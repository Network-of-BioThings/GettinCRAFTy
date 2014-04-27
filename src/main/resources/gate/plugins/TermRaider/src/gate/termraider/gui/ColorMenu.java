/**
 * This file was adapted from PhotoGrid.
 * Copyright (C) 2009-2010, Mark A. Greenwood
 * Copyright (C) 2012, The University of Sheffield
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 2, June 1991
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 **/

package gate.termraider.gui;

import gate.gui.MainFrame;
import gate.resources.img.svg.PaletteIcon;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.MenuSelectionManager;
import javax.swing.border.Border;

public class ColorMenu
{
	protected Border unselectedBorder;
	protected Border selectedBorder;
	protected Border activeBorder;

	protected HashMap<Color, Swatch> standardColors;
	protected HashMap<Color, Swatch> themeColors = null;

	protected Swatch colorPane = new Swatch(new Color(255, 255, 255, 0));

	private Callback callback = null;

	private JComponent menu = null;

	public void setCallback(Callback callback)
	{
		this.callback = callback;
	}

	public ColorMenu(JComponent menu, boolean allowNone, boolean allowAuto)
	{
		this.menu = menu;

		unselectedBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, menu.getBackground());
		selectedBorder = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red), BorderFactory.createMatteBorder(1, 1, 1, 1, menu.getBackground()));
		activeBorder = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, menu.getForeground()), BorderFactory.createMatteBorder(1, 1, 1, 1, menu.getBackground()));

		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Standard Colors")));
		p.setLayout(new GridLayout(2, 8));
		standardColors = new HashMap<Color, Swatch>();

		String[] standard = new String[] { "#000000", "#800000", "#008000", "#808000", "#000080", "#800080", "#008080", "#C0C0C0", "#808080", "#FF0000", "#00FF00", "#FFFF00", "#0000FF", "#FF00FF",
				"#00FFFF", "#FFFFFF" };

		for (String hex : standard)
		{
			Color c = Color.decode(hex);
			Swatch pn = new Swatch(c);
			p.add(pn);
			standardColors.put(c, pn);
		}

		menu.add(p);

		if (allowAuto)
		{
			JMenuItem menuItem = new JMenuItem("Auto");//, new ImageIcon(this.getClass().getClassLoader().getResource("mark/photogrid/resources/images/auto.png")));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					MenuSelectionManager.defaultManager().clearSelectedPath();
					setColor(null);
				}
			});
			menu.add(menuItem);
		}

		if (allowNone)
		{
			JMenuItem menuItem = new JMenuItem("None");//, new ImageIcon(this.getClass().getClassLoader().getResource("mark/photogrid/resources/images/none-small.png")));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					MenuSelectionManager.defaultManager().clearSelectedPath();
					setColor(new Color(255, 255, 255, 0));
				}
			});
			menu.add(menuItem);
		}

		JMenuItem menuItem = new JMenuItem("More Colors...", new PaletteIcon(16, 16));
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0)
			{
			  Color c = JColorChooser.showDialog(MainFrame.getInstance(), "Choose Font Colour", getColor());
				MenuSelectionManager.defaultManager().clearSelectedPath();
				if (c != null) setColor(c);
			}
		});
		menu.add(menuItem);
	}

	public void setThemeColors(Color[] colors)
	{
		if (themeColors != null)
		{
			menu.remove(0);
			themeColors = null;
		}

		if (colors == null || colors.length == 0) return;

		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Theme Colors")));
		int rows = (int) Math.ceil(colors.length / 8d);
		int blanks = (8 * rows) - colors.length;

		p.setLayout(new GridLayout(rows, 8));

		themeColors = new HashMap<Color, Swatch>();

		for (Color c : colors)
		{
			Swatch pn = new Swatch(c);

			if (pn.equals(colorPane)) pn.setSelected(true);

			p.add(pn);
			themeColors.put(c, pn);
		}

		for (int i = 0; i < blanks; ++i)
		{
			p.add(new JPanel());
		}

		menu.add(p, 0);
	}

	public void setColor(Color c)
	{
		// TODO update this so that we add a cell for any color not in the default set
		// Color color = new Color(c.getRGB());
		Swatch swatch = standardColors.get(c);
		if (swatch == null && themeColors != null) swatch = themeColors.get(c);
		if (swatch == null) swatch = new Swatch(c);

		if (colorPane != null) colorPane.setSelected(false);

		colorPane = swatch;
		colorPane.setSelected(true);
		if (callback != null) callback.colorChange(c);
	}

	public Color getColor()
	{
		if (colorPane == null) return null;
		return colorPane.getColor();
	}

	@SuppressWarnings("serial") class Swatch extends JComponent implements MouseListener
	{
		protected Color color;

		protected boolean isSelected;

		public void paint(Graphics g)
		{
			g.setColor(color);
			g.fillRect(0, 0, 16, 16);
			paintBorder(g);
		}

		public Swatch(Color c)
		{
			color = c;
			setBorder(unselectedBorder);
			addMouseListener(this);
		}

		public Color getColor()
		{
			return color;
		}

		@Override public Dimension getPreferredSize()
		{
			return new Dimension(15, 15);
		}

		@Override public Dimension getMaximumSize()
		{
			return getPreferredSize();
		}

		@Override public Dimension getMinimumSize()
		{
			return getPreferredSize();
		}

		public void setSelected(boolean selected)
		{
			isSelected = selected;
			if (isSelected) setBorder(selectedBorder);
			else setBorder(unselectedBorder);
		}

		public boolean isSelected()
		{
			return isSelected;
		}

		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseClicked(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
			setColor(color);
			MenuSelectionManager.defaultManager().clearSelectedPath();
		}

		public void mouseEntered(MouseEvent e)
		{
			setBorder(activeBorder);
		}

		@Override public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((color == null) ? 0 : color.hashCode());
			return result;
		}

		@Override public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			Swatch other = (Swatch) obj;

			if (color == null)
			{
				if (other.color != null) return false;
			}
			else if (!color.equals(other.color)) return false;
			return true;
		}

		public void mouseExited(MouseEvent e)
		{
			setBorder(isSelected ? selectedBorder : unselectedBorder);
		}
	}

	public interface Callback
	{
		public void colorChange(Color c);
	}
}