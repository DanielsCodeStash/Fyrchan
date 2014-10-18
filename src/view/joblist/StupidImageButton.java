package view.joblist;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Funtastic;
import java.util.concurrent.atomic.AtomicInteger;


// This Button is dumb and inefficient... but fun.
public class StupidImageButton
{
    public static Image cancelImage = null;

    private ImageView imageView;
    private ButtonAnimator buttonAnimator = null;

    public StupidImageButton()
    {
        if(cancelImage == null)
        {
            cancelImage = new Image("file:res/cancel_icon.png");
        }

        imageView = new ImageView(cancelImage);
        imageView.setFitWidth(20);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        imageView.setOnMouseEntered(e -> runAnimation());
        imageView.setOnMouseExited(e -> buttonAnimator.cancel());



    }

    private void runAnimation()
    {
        if(buttonAnimator == null)
        {
            startAnimation();
        }
        else
        {
            boolean isAlive = buttonAnimator.continueIfAlive();
            if(!isAlive)
            {
                startAnimation();
            }
        }
    }

    private void startAnimation()
    {
        buttonAnimator = new ButtonAnimator();
        Thread animatorThread = new Thread(buttonAnimator);
        animatorThread.start();
    }

    private class ButtonAnimator implements Runnable
    {
        private double angle = 0;
        private AtomicInteger animationState = new AtomicInteger();
        /*
            0 - running
            1 - exiting
            2 - done
         */

        @Override
        public void run()
        {
            animationState.set(0);
            int localState;
            while( (localState = animationState.get()) != 2  || angle > 0)
            {
                if(localState == 1)
                {
                    double x = (360-angle)/360;
                    double change = Math.pow(x, 2) * 10 + 0.5;
                    //System.out.println("angle: " + angle + ",\tx: " + x + ",\tchange: " + change + "\tstate: " + localState);
                    angle += change;
                }
                else if(localState == 0)
                {
                    //System.out.println("angle: " + angle + "\tstate: 0t");
                    angle += 2;
                }

                if(angle >= 360)
                {
                    angle = 0;
                }
                Platform.runLater(() -> imageView.setRotate(angle));

                if(angle == 0 && localState == 1)
                {
                    animationState.set(2);
                    break;
                }

                try
                {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
            }
        }



        public void cancel()
        {
            animationState.set(1);
        }

        public boolean continueIfAlive()
        {
            int prevState = animationState.getAndSet(0);
            return prevState != 2;
        }
    }

    public void setOnClick(Funtastic fun)
    {
        imageView.setOnMouseClicked(event -> fun.runFun());
    }

    public ImageView getView()
    {
        return imageView;
    }

}
