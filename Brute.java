import java.io.*;
import java.util.*;
import java.util.Arrays; 
import java.awt.Color;

class ColEdge
{
	int u;
	int v;
}

public class Brute
{

	public final static boolean DEBUG = true;

	public final static String COMMENT = "//";

	public static void main( String args[] )
	{
		if( args.length < 1 )
		{
			System.out.println("Error! No filename specified.");
			System.exit(0);
		}


		String inputfile = args[0];

		boolean seen[] = null;

			//! n is the number of vertices in the graph
		int n = -1;

			//! m is the number of edges in the graph
		int m = -1;

			//! e will contain the edges of the graph
		ColEdge e[] = null;

		try 	{ 
			FileReader fr = new FileReader(inputfile);
			BufferedReader br = new BufferedReader(fr);

			String record = new String();

					//! THe first few lines of the file are allowed to be comments, staring with a // symbol.
					//! These comments are only allowed at the top of the file.

					//! -----------------------------------------
			while ((record = br.readLine()) != null)
			{
				if( record.startsWith("//") ) continue;
						break; // Saw a line that did not start with a comment -- time to start reading the data in!
					}

					if( record.startsWith("VERTICES = ") )
					{
						n = Integer.parseInt( record.substring(11) );					
						if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
					}

					seen = new boolean[n+1];	

					record = br.readLine();
					
					if( record.startsWith("EDGES = ") )
					{
						m = Integer.parseInt( record.substring(8) );					
						if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
					}

					e = new ColEdge[m];	

					for( int d=0; d<m; d++)
					{
						if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
						record = br.readLine();
						String data[] = record.split(" ");
						if( data.length != 2 )
						{
							System.out.println("Error! Malformed edge line: "+record);
							System.exit(0);
						}
						e[d] = new ColEdge();
						
						e[d].u = Integer.parseInt(data[0]);
						e[d].v = Integer.parseInt(data[1]);

						seen[ e[d].u ] = true;
						seen[ e[d].v ] = true;
						
						if(DEBUG) System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);

					}

					String surplus = br.readLine();
					if( surplus != null )
					{
						if( surplus.length() >= 2 ) if(DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");						
					}
					
				}
				catch (IOException ex)
				{ 
		        // catch possible io errors from readLine()
					System.out.println("Error! Problem reading file "+inputfile);
					System.exit(0);
				}

				for( int x=1; x<=n; x++ )
				{
					if( seen[x] == false )
					{
						if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
					}
				}

			//! At this point e[0] will be the first edge, with e[0].u referring to one endpoint and e[0].v to the other
			//! e[1] will be the second edge...
			//! (and so on)
			//! e[m-1] will be the last edge
			//! 
			//! there will be n vertices in the graph, numbered 1 to n
//////////////
			//! INSERT YOUR CODE HERE!
//////////////


				//n = # of vertices
				//m = # of edges

				// list contataining the vertices. x = number of the vertex ; y = its color
				// vertices[a][0] = number of vertex a
				// vertices[a][1] = color of vertex a			
				int[][] vertices = new int[n][2];

				//Chromatic number to test
				int chromatic_number = 1 ; 


				System.out.println("Assigning a number to each vertex...");
				//Assign a number of every every vertex ( STARTING AT 1 )
				for (int i = 0; i < n ; i++){
					vertices[i][0] = i+1 ;
				}
				System.out.println("Done.");


				//Create a list with x random colors ( x is the current chromatic number's value)
				int[] colors = new int[chromatic_number];
				for (int i = 0; i<colors.length-1; i++){
					colors[i] = randomNumber();

				}

				//Combinatorics : print number of combinations possible to give each vertex a color
				int combi =  (int) Math.pow(n, chromatic_number);

				System.out.println("number of combinations possible : "+combi);

				//Assign a color to each vertex
				System.out.println();
				for (int i = 0; i < n ; i++){
					
				}

				if (DEBUG) System.out.println(Arrays.deepToString(vertices));



				

			}

		/**Returns a random color.
		@param
		@return an object from the class "Color", chosen randomly ( https://docs.oracle.com/javase/7/docs/api/java/awt/Color.html )
		**/ 
		public static Color randomColor(){
			Random rand = new Random();

			//r,g,b will be a random number btwn 0 - 1 
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();

			//convert to hex.code 
			Color color1 = new Color(r,g,b);

			if (DEBUG){ 
				System.out.println("Colors : "+r);
				System.out.println(g);
				System.out.println(b);
				System.out.println(color1.toString());
			} return color1; 

		}
		/**
		@return a random integer.
		**/
		public static int randomNumber(){

			Random rand = new Random();
			return rand.nextInt();

		}

	}
