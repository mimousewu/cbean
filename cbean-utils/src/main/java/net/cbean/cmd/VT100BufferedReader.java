package net.cbean.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

public class VT100BufferedReader extends BufferedReader {
	PrintStream out;

	public VT100BufferedReader(Reader in) {
		super(in);
	}
	
	public VT100BufferedReader(Reader in, PrintStream out) {
		super(in);
		this.out = out;
	}

	@Override
	public String readLine() throws IOException {
		char[] input = new char[255];
		int index = 0;
		int apdIndex = 0;  // append char do not add
		while(true){
			int a = read();
			if(a == (int)'\r') {
				break;
			}
			if(a== (int)'\n') continue;
			if(a == '\b'){   // backspace
				if(index<=1) { //start of line
					out.print("\33[1C");
					continue;
				}
				out.print("\33[K");
				index--;
			}else if(a == 25 || a == 26 || a == 27 || a == 28){  // ^C
				if(index-1 == 0){
					index = index - 1;
					input[index] = 0;
				}
				apdIndex = 2;
			}else if(a == 248){ //^C
				if(index>1){
					//out.print("\33[2D");
				}
				out.print("\33[K");
				index = 0;
				throw new IllegalStateException(CommandRunner.USER_BREAK);
			}else if(apdIndex > 0){  // ^C
				apdIndex--;
//			}else if(a == 3){  // ^C
//				throw new IllegalStateException(CommandRunner.USER_BREAK);
			}else{
				input[index++] = (char)a;
			}
		}
		return new String(input,0,index);
	}

}
