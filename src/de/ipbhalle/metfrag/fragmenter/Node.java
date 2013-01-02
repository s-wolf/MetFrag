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

import org.openscience.cdk.interfaces.IAtomContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class Node.
 */
public class Node {
	
	/** The parent. */
	private int parent;
	
	/** The fragment. */
	private IAtomContainer fragment;
	
	/** The current node #. */
	private int current;
	
	/** The tree depth. */
	private int treeDepth;
	
	/**
	 * Instantiates a new node.
	 * 
	 * @param parent of the current node
	 * @param fragment the fragment
	 * @param current the current
	 * @param treeDepth the tree depth
	 */
	public Node(int current, int parent, IAtomContainer fragment, int treeDepth)
	{
		this.current = current;
		this.parent = parent;
		this.fragment = fragment;
		this.treeDepth = treeDepth;
	}
	
	/**
	 * Gets the parent node.
	 * 
	 * @return the parent
	 */
	public int getParent()
	{
		return this.parent;
	}
	
	/**
	 * Gets the current node #.
	 * 
	 * @return the parent
	 */
	public int getCurrent()
	{
		return this.current;
	}
	
	/**
	 * Gets the mol.
	 * 
	 * @return the mol
	 */
	public IAtomContainer getMol()
	{
		return this.fragment;
	}

	/**
	 * Sets the tree depth.
	 * 
	 * @param treeDepth the new tree depth
	 */
	public void setTreeDepth(int treeDepth) {
		this.treeDepth = treeDepth;
	}

	/**
	 * Gets the tree depth.
	 * 
	 * @return the tree depth
	 */
	public int getTreeDepth() {
		return treeDepth;
	}

}
