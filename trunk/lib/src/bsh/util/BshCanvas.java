/***********************************************************************************************************************
 *                                                                                                                     *
 *  This file is part of the BeanShell Java Scripting distribution.                                                    *
 *  Documentation and updates may be found at http://www.beanshell.org/                                                *
 *                                                                                                                     *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General  *
 *  Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)  *
 *  any later version.                                                                                                 *
 *                                                                                                                     *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for    *
 *  more details.                                                                                                      *
 *                                                                                                                     *
 *  You should have received a copy of the GNU Lesser General Public License along with this program.                  *
 *  If not, see <http://www.gnu.org/licenses/>.                                                                        *
 *                                                                                                                     *
 *  Patrick Niemeyer (pat@pat.net)                                                                                     *
 *  Author of Learning Java, O'Reilly & Associates                                                                     *
 *  http://www.pat.net/~pat/                                                                                           *
 *                                                                                                                     *
 **********************************************************************************************************************/
package bsh.util;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.This;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

/**
 * Scriptable Canvas with buffered graphics.
 *
 * Provides a Component that: 1) delegates calls to paint() to a bsh method called paint() in a specific NameSpace. 2)
 * provides a simple buffered image maintained by built in paint() that is useful for simple immediate procedural
 * rendering from scripts...  *
 */
public class BshCanvas extends JComponent {

    This ths;
    Image imageBuffer;

    public BshCanvas() {
    }

    public BshCanvas(This ths) {
        this.ths = ths;
    }

    public void paintComponent(Graphics g) {
        // copy buffered image
        if (imageBuffer != null) {
            g.drawImage(imageBuffer, 0, 0, this);
        }

        // Delegate call to scripted paint() method
        if (ths != null) {
            try {
                ths.invokeMethod("paint", new Object[]{g});
            } catch (EvalError e) {
                if (Interpreter.DEBUG) {
                    Interpreter.debug(
                            "BshCanvas: method invocation error:" + e);
                }
            }
        }
    }

    /**
     * Get a buffered (persistent) image for drawing on this component
     */
    public Graphics getBufferedGraphics() {
        Dimension dim = getSize();
        imageBuffer = createImage(dim.width, dim.height);
        return imageBuffer.getGraphics();
    }

    public void setBounds(int x, int y, int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        super.setBounds(x, y, width, height);
    }
}
