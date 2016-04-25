package dk.kea.class2016february.cristianaman.awesometetris;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mancr on 17/02/2016.
 */
public abstract class Game extends Activity implements Runnable, View.OnKeyListener, SensorEventListener
{
    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>();
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Screen screen;
    private Canvas canvas; // Logical virtual screen 320 X 400
    private Bitmap offscreenSurface;
    private Paint paint = new Paint();

    private boolean pressedKeys[] = new boolean[256];
    private KeyEventPool keyEventPool = new KeyEventPool();
    private List<MyKeyEvent> keyEvents = new ArrayList<>();
    private List<MyKeyEvent> keyEventBuffer = new ArrayList<>();

    private TouchHandler touchHandler;
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEvents = new ArrayList<>();
    private List<TouchEvent> touchEventBuffer = new ArrayList<>();

    private float[] accelerometer = new float[3];

    private SoundPool soundPool;

    // end of global variables, start of methods

    public abstract Screen createStartScreen();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // probably not needed
//        this.getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // Get the SurfaceView object i.e. dedicated drawing
        surfaceView = new SurfaceView(this);
        // Creates an actual view (Window) in the Activity class
        setContentView(surfaceView);
        // Get holder that gives access to the surfaceView's
        surfaceHolder = surfaceView.getHolder();


        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.soundPool = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);

        screen = createStartScreen();

        if (surfaceView.getWidth() > surfaceView.getHeight())
        {
            setOffsetSurface(480, 320);
        } else
        {
            setOffsetSurface(320, 480);
        }

        surfaceView.setFocusableInTouchMode(true);
        surfaceView.requestFocus();
        surfaceView.setOnKeyListener(this);

        touchHandler = new MultiTouchHandler(surfaceView, touchEventBuffer, touchEventPool);

        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
        {
            Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    //This is the main method for the game loop
    public void run()
    {
        long lastTime = System.nanoTime();
        long currentTime;

        while (true)
        {
            synchronized (stateChanges)
            {
                for (int i = 0; i < stateChanges.size(); i++)
                {
                    state = stateChanges.get(i);
                    if (state == State.Disposed)
                    {
                        Log.d("Game", "State is disposed");
                        if (screen != null) screen.dispose();
                    } else if (state == State.Paused)
                    {
                        Log.d("Game", "State is paused");
                        if (screen != null) screen.pause();
                    } else if (state == State.Resumed)
                    {
                        Log.d("Game", "State is resumed");
                        if (screen != null) screen.resume();
                        state = State.Running;
                    }
                }
                stateChanges.clear();
            }
            if (state == State.Running)
            {
                if (!surfaceHolder.getSurface().isValid()) continue;
                Canvas physicalCanvas = surfaceHolder.lockCanvas();

                fillEvents();
                currentTime = System.nanoTime();
                // here we should do some drawings on the screen
                if (screen != null)
                    screen.update((currentTime - lastTime)/1000000000.0f);
                lastTime = currentTime;
                freeEvents();
                src.left = 0;
                src.top = 0;
                src.right = offscreenSurface.getWidth() - 1;
                src.bottom = offscreenSurface.getHeight() - 1;

                dst.left = 0;
                dst.top = 0;
                dst.right = surfaceView.getWidth() - 1;
                dst.bottom = surfaceView.getHeight() - 1;

                physicalCanvas.drawBitmap(offscreenSurface, src, dst, null);

                surfaceHolder.unlockCanvasAndPost(physicalCanvas);
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        synchronized (stateChanges)
        {
            if (isFinishing())
            {
                stateChanges.add(stateChanges.size(), State.Disposed);
                ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
            } else
            {
                stateChanges.add(stateChanges.size(), State.Paused);
            }
        }

        try
        {
            mainLoopThread.join();
        } catch (InterruptedException e){}

        if(isFinishing()) soundPool.release();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mainLoopThread = new Thread(this);
        mainLoopThread.start();

        synchronized (stateChanges)
        {
            stateChanges.add(stateChanges.size(), State.Resumed);
        }
    }

    public void setScreen(Screen newScreen)
    {
        if (this.screen != null)
            this.screen.dispose();
        this.screen = newScreen;
    }

    public void setOffsetSurface(int width, int height)
    {
        if (offscreenSurface != null) offscreenSurface.recycle();
        offscreenSurface = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(offscreenSurface);
    }

    public Typeface loadFont(String fileName)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), fileName);
        if (font == null)
        {
            throw new RuntimeException("Could not load font from asset " + fileName);
        }
        return font;
    }

    public void drawText(Typeface font, String text, int x, int y, int color, int size)
    {
        paint.setTypeface(font);
        paint.setTextSize(size);
        paint.setColor(color);
        canvas.drawText(text, x, y + size, paint);
    }

    // It is standard to use bitmap graphics in Android
    public Bitmap loadBitmap(String fileName)
    {
        InputStream in = null;
        Bitmap bitmap;

        try
        {
            in = getAssets().open(fileName);        // Get input stream from the standard method in Activity class
            bitmap = BitmapFactory.decodeStream(in); // Decode input stream
            if (bitmap == null)  // Verify success
                throw new RuntimeException("Could not get a bitmap from the file " + fileName);
            return bitmap;

        } catch (IOException e)
        {
            throw new RuntimeException("Could not load the file " + fileName);

        } finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (IOException e)
                {
                    Log.d("closing inputstream", "Shit");
                }
            }
        }
    }

    public void drawBitmap(Bitmap bitmap, int x, int y)
    {
        if (canvas == null) return;

        canvas.drawBitmap(bitmap, x, y, null);
    }

    Rect src = new Rect();
    Rect dst = new Rect();

    /**
     * Draw parts of a bitmap
     *
     * @param bitmap    object to be drawn
     * @param x         coordinate on screen
     * @param y
     * @param srcX      coordinate on picture (bitmap)
     * @param srcY
     * @param srcWidth  size of part of the picture
     * @param srcHeight
     */
    public void drawBitmap(Bitmap bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
    {
        if (canvas == null) return;
        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWidth;
        src.bottom = srcY + srcHeight;

        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;

        canvas.drawBitmap(bitmap, src, dst, null);
    }

    public Music loadMusic(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            return new Music(assetFileDescriptor);
        } catch (IOException e)
        {
            throw new RuntimeException("Could not load music file: " + fileName);
        }
    }

    public Sound loadSound(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            Sound sound = new Sound(soundPool, soundId);
            return sound;
        } catch (IOException e)
        {
            throw new RuntimeException("Could not load sound file: " + fileName + " BAD ERROR!!!");
        }
    }

    public void clearFramebuffer(int color)
    {
        if (canvas != null) canvas.drawColor(color);
    }

    public int getFramebufferWidth()
    {
        return surfaceView.getWidth();
    }

    public int getFrameBufferHeight()
    {
        return surfaceView.getHeight();
    }

    public int getOffscreenWidth()
    {
        return offscreenSurface.getWidth();
    }

    public int getOffscreenHeight()
    {
        return offscreenSurface.getHeight();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            pressedKeys[keyCode] = true;
        } else if (event.getAction() == KeyEvent.ACTION_UP)
        {
            pressedKeys[keyCode] = false;
        }

        return false;
    }

    public boolean isKeyPressed(int keyCode)
    {
        return pressedKeys[keyCode];
    }

    /**
     * @param pointer number of touch pointer (fingers on screen)
     * @return
     */
    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }

    public boolean isTouchEventUp()
    {
        List<TouchEvent> events = getTouchEvents();
        int stop = events.size();
        for (int i = 0; i < stop; i++)
        {
            if (events.get(i).type == TouchEvent.TouchEventType.Up)
            {
                return true;
            }
        }
        return false;
    }

    public int getTouchX(int pointer)
    {
        return (int) ((float) touchHandler.getTouchX(pointer) * (float) offscreenSurface.getWidth() / (float) surfaceView.getWidth());
    }

    public int getTouchY(int pointer)
    {
        return (int) ((float) touchHandler.getTouchY(pointer) * (float) offscreenSurface.getHeight() / (float) surfaceView.getHeight());
    }

    public List<MyKeyEvent> getKeyEvents()
    {
        return keyEvents;
    }
    public List<TouchEvent> getTouchEvents()
    {
        return touchEvents;
    }

    private void fillEvents()
    {
        synchronized (keyEventBuffer)
        {
            int stop = keyEventBuffer.size();
            for (int i = 0; i < stop; i++)
            {
                keyEvents.add(keyEventBuffer.get(i));
            }
            keyEventBuffer.clear();
        }

        synchronized (touchEventBuffer)
        {
            int stop = touchEventBuffer.size();
            for (int i = 0; i < stop; i++)
            {
                touchEvents.add(touchEventBuffer.get(i));
            }
            touchEventBuffer.clear();
        }

    }

    private void freeEvents()
    {
        synchronized (keyEvents)
        {
            int stop = keyEvents.size();
            for (int i = 0; i < stop; i++)
            {
                keyEventPool.free(keyEvents.get(i));
            }
            keyEvents.clear();
        }

        synchronized (touchEvents)
        {
            int stop = touchEvents.size();
            for (int i = 0; i < stop; i++)
            {
                touchEventPool.free(touchEvents.get(i));
            }
            touchEvents.clear();
        }
    }

    public float[] getAccelerometer()
    {
        return accelerometer;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        System.arraycopy(event.values, 0, accelerometer, 0, 3);
    }
}