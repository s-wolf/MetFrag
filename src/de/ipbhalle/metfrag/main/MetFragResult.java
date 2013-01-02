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

package de.ipbhalle.metfrag.main;

import java.util.List;
import java.util.Vector;

import org.openscience.cdk.interfaces.IAtomContainer;

import de.ipbhalle.metfrag.spectrum.PeakMolPair;

public class MetFragResult {
	
	private String candidateID = "";
	private IAtomContainer structure = null;
	private double score = 0.0;
	private int peaksExplained = 0;
	private Vector<PeakMolPair> fragments = null;
	
	/**
	 * Instantiates a new MetFrag result.
	 * 
	 * @param candidateID the candidate id
	 * @param structure the structure
	 * @param score the score
	 * @param peaksExplained the peaks explained
	 */
	public MetFragResult( String candidateID, IAtomContainer structure, double score, int peaksExplained)
	{
		this.setCandidateID(candidateID);
		this.setScore(score);
		this.setStructure(structure);
		this.setPeaksExplained(peaksExplained);
	}
	
	/**
	 * Instantiates a new MetFrag result.
	 * 
	 * @param candidateID the candidate id
	 * @param structure the structure
	 * @param score the score
	 * @param peaksExplained the peaks explained
	 */
	public MetFragResult( String candidateID, IAtomContainer structure, double score, int peaksExplained, Vector<PeakMolPair> fragments)
	{
		this.setCandidateID(candidateID);
		this.setScore(score);
		this.setStructure(structure);
		this.setPeaksExplained(peaksExplained);
		this.setFragments(fragments);
	}

	public void setPeaksExplained(int peaksExplained) {
		this.peaksExplained = peaksExplained;
	}

	public int getPeaksExplained() {
		return peaksExplained;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getScore() {
		return score;
	}

	public void setStructure(IAtomContainer structure) {
		this.structure = structure;
	}

	public IAtomContainer getStructure() {
		return structure;
	}

	public void setCandidateID(String candidateID) {
		this.candidateID = candidateID;
	}

	public String getCandidateID() {
		return candidateID;
	}

	public void setFragments(Vector<PeakMolPair> fragments) {
		this.fragments = fragments;
	}

	public Vector<PeakMolPair> getFragments() {
		return fragments;
	}

}
