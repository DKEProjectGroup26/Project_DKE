import java.io.*;
import java.util.*;
import java.util.Arrays;
import java.util.ArrayList;

		class ColEdge
			{
			int u;
			int v;
			}
		
public class ReadGraph
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

			//! INSERT YOUR CODE HERE!
			/*Create an array matrix(adjacent matrix)
			Use Integer instead of int to make the array can be transformed into ArrayList*/
			Integer [][] matrix = new Integer[n][n];
			//Initialize the chromatic number
			int chromaticNumber = 0;
			/*Because it's now Integer, which cannot be initialize into 0 automatically.
			This step is to initialize it.*/
			for(int i=0;i<n;i++){
				Arrays.fill(matrix[i],0);
			}
			/*put values into matrix as rules of adjacent matrix, and to make it easier and less loops later,
			let the values int the upper diagnal of the matrix.*/
			for(int i=0;i<m;i++){
				if(e[i].u<=e[i].v)
					matrix[e[i].u-1][e[i].v-1]=1;
				else
					matrix[e[i].v-1][e[i].u-1]=1;
			}
			// i controls the columns and j controls the rows
			for(int i=0; i<n;i++){
				//matrix[i][i]is the colour that the vertax i+1 can choose
				matrix[i][i]=1;
				/*Use this for loop to check the vertax i+1 can't be in the same with which other vertax(by checking the column i),
				and put the color the vertax i+1 can't be into the row j in ascending order */
				for(int j=0;j<i;j++){
					if(matrix[j][i]==1)
						matrix[i][matrix[j][j]-1]=matrix[j][j];
				}
				//Transform array to ArrayList to find the index of 0 without any loop
					ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(matrix[i]));
					// Using 0 is to find out which color that the vertax i+1 could be
					int a =list.indexOf(0);
					if(a>=0){
					 if(a<i)
						matrix[i][i]=a+1;
					 //When there's no other 0 in the lower diagnal, then the color of vertax i+1 should be a new color
					 else
						matrix[i][i]=i+1;
					}else{
						matrix[i][i]=i+1;
					}
					//Find out the maximum number in the diagnal line,and then it is the chromatic number
					chromaticNumber = Math.max(chromaticNumber,matrix[i][i]);
			}
			
			
			
			System.out.println("The chromatic number of this graph is "+chromaticNumber);		
			
		}

}
