/**
 * @(#)Asteroid.java
 *
 *
 * @author 
 * @version 1.00 2015/10/3
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Asteroid {
	public Image colour; //colour of soldier and the blood icon displayed when they die
	public int ax, ay, directionx, directiony, type; //coordinates of soldier
	
	public Asteroid(int x, int y, int dx, int dy, int t){
		//takes in number, images, coordinates, and image of blood for the soldier
		ax = x;
		ay = y;
		directionx = dx;
		directiony = dy;
		type = t;
	}
}