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
package de.ipbhalle.metfrag.massbankParser;

import java.util.Vector;
import java.util.Iterator;
import java.util.Arrays;


public class Compound implements java.io.Serializable {
  
  private Molecule formula;
  private String name;
  private int mode;
  private double mass;
  private double focusedMass;
  private String instrument;
  public int i;
  
  private Vector<Spectrum> spectra;
  
  public Compound(Molecule formula, String name, int mode, double mass,
                  double focusedMass, String instrument, Vector<Spectrum> spectra){
  this.formula = formula;
  this.name = name;
  this.mode = mode;
  this.mass = mass;
  this.focusedMass = focusedMass;
  this.instrument = instrument;
  this.spectra = spectra; 
  }
  
  public Compound(String name, int mode, double focusedMass, String instrument,
                  Vector<Spectrum> spectra){
  this.formula = null;
  this.name = name;
  this.mode = mode;
  this.mass = focusedMass;
  this.focusedMass = focusedMass;
  this.instrument = instrument;
  this.spectra = spectra; 
  }

  public Molecule getFormula(){
    return formula;
  }
  
  public String getName(){
    return name;
  }
  
  public int getMode(){
    return mode;
  }
  
  public double getMass(){
    return mass;
  }
  
  public double getFocusedMass(){
    return focusedMass;
  }
  
  public String getInstrument(){
    return instrument;
  }
  
  public Vector<Spectrum> getSpectra(){
    return spectra;
  }
  
  public void setFormula(Molecule formula){
  this.formula = formula;
  }
  public void setName(String name){
  this.name = name;
  }
  public void setMode(int mode){
  this.mode = mode;
  }
  public void setMass(double mass){
  this.mass = mass;
  }
  public void setFocusedMass(double focusedMass){
  this.focusedMass = focusedMass;
  }
  public void setSpectra(Vector<Spectrum> spectra){
  this.spectra = spectra;
  }
  
  public void calcRelIntensities(){
    for (Spectrum spectrum : spectra){
      spectrum.calcRelIntensities();
    }
  }
  
  public void calculateSNR(){
    for (Spectrum spectrum : spectra){
      spectrum.calculateSNR();
    }
  }
  
  public void calculateRaw(){
    for (Spectrum spectrum : spectra){
      spectrum.calculateRaw();
    }
  } 

  public void strictFilter(double threshold){
    for (Spectrum spectrum : spectra){
      spectrum.strictFilter(threshold);
    }
  } 
  
  public Vector<Peak> mergePeaks(double threshold, boolean intenseMerge){
    int numberOfPeaks = 0, index = 0, indexSmallestPeak = 0; // index of the smallest peak belonging in the merge group
    boolean mergedsth = true;
    int refPeakIndex= 0, mergeRange = 0;
    Peak refPeak = null, mergePeak = null;
    for (Iterator<Spectrum> iter = spectra.iterator(); iter.hasNext();){
      numberOfPeaks += iter.next().getPeaks().size();
    }
    Peak[] peaks = new Peak[numberOfPeaks];
    for (Iterator<Spectrum> iter = spectra.iterator(); iter.hasNext();){
      for (Iterator<Peak> peakIter = iter.next().getPeaks().iterator(); peakIter.hasNext();){
        peaks[index] = peakIter.next();
        ++index;
      }
    }
    Arrays.sort(peaks);

    double massUntilMerge = peaks[0].getMass(); //the highest mass we currently merge to.
    for (index = 0; index <= peaks.length; ++index){ // for the last merge, equality is necessary
      if (index == peaks.length || peaks[index].getMass() > massUntilMerge + threshold){
        // next mass is not in merge range, merge the smaller ones
        mergedsth = true;
        refPeakIndex = indexSmallestPeak;
        while (mergedsth){
          mergedsth = false; // not yet anything merged in this loop
          refPeak = null;
          while (refPeak == null){
            refPeak = peaks[refPeakIndex];
            ++refPeakIndex;
            // Restart at the beginning of the merge range, til no more peaks are merged. 
            if (refPeakIndex >= index) refPeakIndex = indexSmallestPeak;
          }
          for (mergeRange = indexSmallestPeak; mergeRange < index; ++mergeRange){
            mergePeak = peaks[mergeRange];
            if(mergePeak != null && mergePeak != refPeak && refPeak.isEnergyAdjacent(mergePeak)){
              
              //refPeak.setMass(mergePeak.getIntensity()*mergePeak.getMass() + refPeak.getIntensity()*refPeak.getMass()/mergePeak.getIntensity()+refPeak.getIntensity());
              if (intenseMerge) {
              	refPeak.setMass(refPeak.getIntensity() > mergePeak.getIntensity()?refPeak.getMass():mergePeak.getMass());
              } else {
              	refPeak.setMass((refPeak.getMass()+mergePeak.getMass())/2);
              }
              refPeak.setHighestEnergy(Math.max(mergePeak.getHighestEnergy(), refPeak.getHighestEnergy()));
              refPeak.setLowestEnergy(Math.min(mergePeak.getLowestEnergy(), refPeak.getLowestEnergy()));
              refPeak.setIntensity(Math.max(mergePeak.getIntensity(), refPeak.getIntensity()));
              refPeak.setRelIntensity(Math.max(mergePeak.getRelIntensity(), refPeak.getRelIntensity()));
              refPeak.setRelIntensityDB(Math.max(mergePeak.getRelIntensityDB(), refPeak.getRelIntensityDB()));
              peaks[mergeRange] = null;
              
              mergedsth = true;
            }
          }
          // Restart at the beginning of the merge range, til no more peaks are merged. 
          if (refPeakIndex >= index) refPeakIndex = indexSmallestPeak;
        }
        // set the smallest peak in the new merge range to be this peak
        indexSmallestPeak = index;
      }
      if (index != peaks.length) massUntilMerge = peaks[index].getMass();
    }
    
    Vector<Peak> mergedPeaks = new Vector<Peak>();
    for (index = 0; index < peaks.length; ++index){
      if (peaks[index] != null) mergedPeaks.add(peaks[index]);
    }
    
    return mergedPeaks;
  } 
}
