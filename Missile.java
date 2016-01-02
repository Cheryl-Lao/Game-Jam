/**
 * @(#)Missile.java
 *
 *
 * @author 
 * @version 1.00 2015/10/3
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Missile {
	public int posx, posy; //coordinates of missile

    public Missile(int sx, int sy) { //takes in coordinates of ship location and direction of fire
    	posx = sx;
		posy = sy;
    }
}