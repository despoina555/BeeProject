package bee.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import bee.demo.graphTemplate.EmbeddedGraph;

public class BEIOhandler
{
	public static void writeToFile(String fileName,EmbeddedGraph emG)
	{
		try
		{
			if(fileName.endsWith(".be"))
			{
				File file=new File(fileName);
				FileWriter fw=new FileWriter(file);
				BufferedWriter bw=new BufferedWriter(fw);
				bw.write("//EMBEDDED GRAPH "+fileName);
				bw.newLine();
				bw.write("//NODE-ORDER");

				bw.newLine();
				bw.write("{");
				bw.newLine();
				int[] nodeOrder=emG.getNodeOrder();
				for(int i=0;i<nodeOrder.length;i++)
				{
					bw.write("Node-"+nodeOrder[i]);
					bw.newLine();
				}
				bw.write("}");
				bw.newLine();
				bw.write("//EDGE-COLORING");
				bw.newLine();
				bw.write("{");
				bw.newLine();
				int[][] embeddedEdges=emG.getEmbeddedEdges();
				for(int i=0;i<embeddedEdges.length;i++)
				{
					int[] edge=embeddedEdges[i];
					bw.write("Edge-[");
					for(int j=0;j<edge.length-1;j++)
					{
						bw.write(""+edge[j]+",");
					}
					bw.write(edge[edge.length-1]+"]");
					bw.newLine();
				}
				bw.write("}");
				bw.newLine();
				bw.write("//END OF FILE");
				bw.close();
			}

		}
		catch(Exception e)
		{
			System.out.println("ERROR");
			e.printStackTrace();
		}
	}

	public static EmbeddedGraph readFromFile(String fileName) throws Exception
	{
		try
		{
			if(fileName.endsWith(".be"))
			{
				File file=new File(fileName);
				FileReader fr=new FileReader(file);
				BufferedReader br=new BufferedReader(fr);
				List<Integer> nodeList=new ArrayList<>();
				List<int[]> edgeList=new ArrayList<>();

				String line=br.readLine();
				while(!line.startsWith("//NODE-ORDER"))
				{
					line=br.readLine();
				}
				while(!line.startsWith("{"))
				{
					line=br.readLine();
				}
				line=br.readLine();
				while(!line.startsWith("}"))
				{
					if(!line.startsWith("Node-"))
					{
						throwError();
					}
					line=line.substring(5);
					Integer nodeIndex=Integer.parseInt(line);
					nodeList.add(nodeIndex);
					line=br.readLine();
				}
				//read edges
				while(!line.startsWith("//EDGE-COLORING"))
				{
					line=br.readLine();
				}
				while(!line.startsWith("{"))
				{
					line=br.readLine();
				}
				line=br.readLine();
				while(!line.startsWith("}"))
				{
					if(!line.startsWith("Edge-"))
					{
						throwError();
					}
					line=line.substring(6);
					StringTokenizer tokenizer=new StringTokenizer(line, ",]");
					if(tokenizer.countTokens()!=3)
					{
						throwError();
					}
					int[] edge=new int[3];
					int pos=0;
					while(tokenizer.hasMoreTokens())
					{
						String token=tokenizer.nextToken();
						Integer edgeIndex=Integer.parseInt(token);
						edge[pos]=edgeIndex;
						pos++;
					}
					edgeList.add(edge);
					line=br.readLine();
				}
				if(!line.startsWith("}"))
				{
					throwError();
				}
				int[] nodeOrder=new int[nodeList.size()];
				{
					for(int i=0;i<nodeOrder.length;i++)
					{
						nodeOrder[i]=nodeList.get(i).intValue();
					}
				}
				int[][] embeddedEdges=new int[edgeList.size()][];
				{
					for(int i=0;i<embeddedEdges.length;i++)
					{
						embeddedEdges[i]=edgeList.get(i);
					}
				}
				fr.close();
				br.close();
				EmbeddedGraph graph=new EmbeddedGraph(nodeOrder, embeddedEdges);
				return graph;
			}
		}
		catch(Exception e)
		{
			System.out.println("ERROR OPENING FILE");
			e.printStackTrace();
		}
		return null;
	}

	private static void throwError() throws Exception
	{
		throw new Exception("invalid input");
	}
}
