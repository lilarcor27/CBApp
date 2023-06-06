package com.lilarcor.planner;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class MyRandom
{	
	static int next = 1;

	static int myRandom() {
		next = next * 5336100 + 12345;
		next = (next < 0 ? next *= -1 : next);
		return (next / 65535) % 32768;
	}

	static void mysrand(int seed) {
		next = seed;
	}
}