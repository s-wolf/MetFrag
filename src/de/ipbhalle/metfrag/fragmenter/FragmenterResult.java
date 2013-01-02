/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package de.ipbhalle.metfrag.fragmenter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.openscience.cdk.interfaces.IAtomContainer;

import de.ipbhalle.metfrag.scoring.OptimizationMatrixEntry;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.PeakMolPair;

public class FragmenterResult {
	
	private StringBuilder completeLog = null;
	private Map<String, Double> mapCandidateToEnergy = null;
	private Map<String, Double> mapCandidateToHydrogenPenalty = null;
	private Map<String, Double> mapCandidateToPartialChargesDiff = null;
	private Map<Double, Vector<String>> realScoreMap = null;
	private Map<Integer, List<String>> scoreMap = null;
	private Map<String, IAtomContainer> mapCandidateToStructure = null;
	private Map<String, Vector<PeakMolPair>> mapCandidateToFragments = null;
	private Map<String, List<OptimizationMatrixEntry>> candidateToOptimizationMatrixEntries = null;

	
	public FragmenterResult()
	{
		completeLog = new StringBuilder();
		mapCandidateToEnergy = Collections.synchronizedMap(new HashMap<String, Double>());
		mapCandidateToHydrogenPenalty = Collections.synchronizedMap(new HashMap<String, Double>());
		mapCandidateToPartialChargesDiff = Collections.synchronizedMap(new HashMap<String, Double>());
		realScoreMap = Collections.synchronizedMap(new HashMap<Double, Vector<String>>());
		scoreMap = Collections.synchronizedMap(new HashMap<Integer, List<String>>());
		mapCandidateToStructure = Collections.synchronizedMap(new HashMap<String, IAtomContainer>());
		mapCandidateToFragments = Collections.synchronizedMap(new HashMap<String, Vector<PeakMolPair>>());
		candidateToOptimizationMatrixEntries = Collections.synchronizedMap(new HashMap<String, List<OptimizationMatrixEntry>>());
	}

	/**
	 * Add a String to complete log
	 * 
	 * @param add the new complete log
	 */
	public synchronized void addToCompleteLog(String add) {
		this.completeLog.append(add);
	}

	public StringBuilder getCompleteLog() {
		return completeLog;
	}

	public void setMapCandidateToEnergy(Map<String, Double> mapCandidateToEnergy) {
		this.mapCandidateToEnergy = mapCandidateToEnergy;
	}

	public Map<String, Double> getMapCandidateToEnergy() {
		return mapCandidateToEnergy;
	}

	public void setMapCandidateToHydrogenPenalty(
			Map<String, Double> mapCandidateToHydrogenPenalty) {
		this.mapCandidateToHydrogenPenalty = mapCandidateToHydrogenPenalty;
	}

	public Map<String, Double> getMapCandidateToHydrogenPenalty() {
		return mapCandidateToHydrogenPenalty;
	}

	public void setMapCandidateToPartialChargesDiff(
			Map<String, Double> mapCandidateToPartialChargesDiff) {
		this.mapCandidateToPartialChargesDiff = mapCandidateToPartialChargesDiff;
	}

	public Map<String, Double> getMapCandidateToPartialChargesDiff() {
		return mapCandidateToPartialChargesDiff;
	}

	public void setRealScoreMap(Map<Double, Vector<String>> realScoreMap) {
		this.realScoreMap = realScoreMap;
	}

	public Map<Double, Vector<String>> getRealScoreMap() {
		return realScoreMap;
	}

	public void setScoreMap(Map<Integer, List<String>> scoreMap) {
		this.scoreMap = scoreMap;
	}

	public Map<Integer, List<String>> getScoreMap() {
		return scoreMap;
	}

	public void setMapCandidateToStructure(Map<String, IAtomContainer> mapCandidateToStructure) {
		this.mapCandidateToStructure = mapCandidateToStructure;
	}

	public Map<String, IAtomContainer> getMapCandidateToStructure() {
		return mapCandidateToStructure;
	}

	public void setMapCandidateToFragments(Map<String, Vector<PeakMolPair>> mapCandidateToFragments) {
		this.mapCandidateToFragments = mapCandidateToFragments;
	}

	public Map<String, Vector<PeakMolPair>> getMapCandidateToFragments() {
		return mapCandidateToFragments;
	}

	public void setCandidateToOptimizationMatrixEntries(
			Map<String, List<OptimizationMatrixEntry>> candidateToOptimizationMatrixEntries) {
		this.candidateToOptimizationMatrixEntries = candidateToOptimizationMatrixEntries;
	}

	public Map<String, List<OptimizationMatrixEntry>> getCandidateToOptimizationMatrixEntries() {
		return candidateToOptimizationMatrixEntries;
	}

}
