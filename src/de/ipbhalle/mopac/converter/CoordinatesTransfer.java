package de.ipbhalle.mopac.converter;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

public class CoordinatesTransfer {
	
	public static IAtomContainer transferCoordinates(IAtomContainer source, IAtomContainer target)
	{
		for (int i = 0; i < target.getAtomCount(); i++) {
			target.getAtom(i).setPoint3d(source.getAtom(i).getPoint3d());
		}
		
		return target;
	}

}
