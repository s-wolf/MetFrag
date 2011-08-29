package de.ipbhalle.metfrag.mainTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import de.ipbhalle.metfrag.similarity.Similarity;
import de.ipbhalle.metfrag.similarity.SimilarityGroup;
import de.ipbhalle.metfrag.similarity.TanimotoClusterer;

public class ClusterCandidates {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		String targetCandidates = "/home/swolf/MOPAC/Hill_PubChem_Formula/pubchem/";
//		String pathToWrite = "/home/swolf/MOPAC/Hill_PubChem_Formula/pubchemClustered/";
		
		String targetCandidates = "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/pubchem/";
		String pathToWrite = "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/pubchemClustered/";
			
		
		File[] files = new File(targetCandidates).listFiles();
		for (int i = 0; i < files.length; i++) {
			//now iterate in subfolder
			if(files[i].isDirectory())
			{
				try
				{
					File[] candidates = files[i].listFiles();
					Map<String, IAtomContainer> testMap = new HashMap<String, IAtomContainer>();
					List<String> cands = new ArrayList<String>();
					for (int j = 0; j < candidates.length; j++) {


						MDLV2000Reader reader = new MDLV2000Reader(new FileReader(candidates[j]));
				        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
				        List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
						testMap.put(candidates[j].getName().split("\\.")[0], containersList.get(0));
						cands.add(candidates[j].getName().split("\\.")[0]);
					}
					
					Similarity sim = new Similarity(testMap, true, false);
					TanimotoClusterer tanimoto = new TanimotoClusterer(sim.getSimilarityMatrix(), sim.getCandidateToPosition());
					List<SimilarityGroup> clusteredCpds = tanimoto.clusterCandididates(cands, 0.95f);
					List<SimilarityGroup> clusteredCpdsCleaned = tanimoto.getCleanedClusters(clusteredCpds);
					
					for (SimilarityGroup similarityGroup : clusteredCpdsCleaned) {
						String candidatesClustered = "";
						//cluster
						if(similarityGroup.getSimilarCompounds().size() > 1)
						{
							for (int k = 0; k < similarityGroup.getSimilarCompounds().size(); k++) {
								if(k == similarityGroup.getSimilarCompounds().size() - 1)
								{
									candidatesClustered += similarityGroup.getSimilarCompounds().get(k).getCompoundID();
								}
								else
								{
									candidatesClustered += similarityGroup.getSimilarCompounds().get(k).getCompoundID() + "_";
								}
							}
						}
						//single
						else
						{
							candidatesClustered = similarityGroup.getSimilarCompounds().get(0).getCompoundID();
						}
						String filename = similarityGroup.getSimilarCompounds().get(0).getCompoundID();
						//write to file single or clustered candidates!
						SDFWriter writer = new SDFWriter();
						try {
							new File(pathToWrite + files[i].getName() + "/").mkdirs();
							writer.setWriter(new FileWriter(new File(pathToWrite + files[i].getName() + "/" + filename + ".sdf")));
							IAtomContainer temp = testMap.get(similarityGroup.getSimilarCompounds().get(0).getCompoundID());
							temp.setProperty("candidatesClustered", candidatesClustered);
							writer.write(temp);
							writer.close();
						} catch (IOException e) {
							System.err.println("Can't write structure to file!");
							e.printStackTrace();
						}

					}
				}
				catch(FileNotFoundException e)
				{
					System.err.println("File not found! " + e.getMessage());
					e.printStackTrace();
				} catch (CDKException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
}
