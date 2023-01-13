package vue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FullScreenEffect implements ActionListener{
    private boolean Am_I_In_FullScreen = false;
    private int PrevX, PrevY, PrevWidth, PrevHeight;
    private JFrame frame;
    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub


        if(Am_I_In_FullScreen == false){

            PrevX = frame.getX();
            PrevY = frame.getY();
            PrevWidth = frame.getWidth();
            PrevHeight = frame.getHeight();

            frame.dispose(); //Destroys the whole JFrame but keeps organized every Component
            //Needed if you want to use Undecorated JFrame
            //dispose() is the reason that this trick doesn't work with videos
            frame.setUndecorated(true);

            frame.setBounds(0,0,frame.getToolkit().getScreenSize().width,frame.getToolkit().getScreenSize().height);
            frame.setVisible(true);
            Am_I_In_FullScreen = true;
        }
        else{
            frame.setVisible(true);

            frame.setBounds(PrevX, PrevY, PrevWidth, PrevHeight);
            frame.dispose();
            frame.setUndecorated(false);
            frame.setVisible(true);
            Am_I_In_FullScreen = false;
        }
    }
}
