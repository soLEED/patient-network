/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.phenotips.data.similarity.permissions.internal;

import org.phenotips.data.permissions.AccessLevel;

import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.localization.LocalizationContext;
import org.xwiki.localization.LocalizationManager;
import org.xwiki.localization.Translation;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.renderer.BlockRenderer;
import org.xwiki.rendering.renderer.printer.WikiPrinter;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link MatchAccessLevel matchable access level}.
 * 
 * @version $Id$
 */
public class MatchAccessLevelTest
{
    @Rule
    public final MockitoComponentMockingRule<AccessLevel> mocker =
        new MockitoComponentMockingRule<AccessLevel>(MatchAccessLevel.class);

    /** Basic test for {@link AccessLevel#getName()}. */
    @Test
    public void getName() throws ComponentLookupException
    {
        Assert.assertEquals("match", this.mocker.getComponentUnderTest().getName());
    }

    /** Basic test for {@link AccessLevel#isAssignable()}. */
    @Test
    public void isAssignable() throws ComponentLookupException
    {
        Assert.assertFalse(this.mocker.getComponentUnderTest().isAssignable());
    }

    /** Basic test for {@link AccessLevel#getLabel()}. */
    @Test
    public void getLabel() throws ComponentLookupException
    {
        LocalizationContext lc = this.mocker.getInstance(LocalizationContext.class);
        LocalizationManager lm = this.mocker.getInstance(LocalizationManager.class);
        Translation t = mock(Translation.class);
        Block b = mock(Block.class);
        BlockRenderer r = this.mocker.getInstance(BlockRenderer.class, "plain/1.0");
        when(lc.getCurrentLocale()).thenReturn(Locale.US);
        when(lm.getTranslation("phenotips.permissions.accessLevels.match.label", Locale.US)).thenReturn(t);
        when(t.render(Locale.US)).thenReturn(b);
        Mockito.doAnswer(new Answer<Object>()
        {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                WikiPrinter printer = (WikiPrinter) invocation.getArguments()[1];
                printer.print("Match");
                return null;
            }
        }).when(r).render(same(b), any(WikiPrinter.class));
        Assert.assertEquals("Match", this.mocker.getComponentUnderTest().getLabel());
    }

    /** {@link AccessLevel#getLabel()} returns the name when a translation isn't found. */
    @Test
    public void getLabelWithoutTranslation() throws ComponentLookupException
    {
        Assert.assertEquals("match", this.mocker.getComponentUnderTest().getLabel());
    }

    /** Basic test for {@link AccessLevel#getDescription()}. */
    @Test
    public void getDescription() throws ComponentLookupException
    {
        LocalizationContext lc = this.mocker.getInstance(LocalizationContext.class);
        LocalizationManager lm = this.mocker.getInstance(LocalizationManager.class);
        Translation t = mock(Translation.class);
        Block b = mock(Block.class);
        BlockRenderer r = this.mocker.getInstance(BlockRenderer.class, "plain/1.0");
        when(lc.getCurrentLocale()).thenReturn(Locale.US);
        when(lm.getTranslation("phenotips.permissions.accessLevels.match.description", Locale.US)).thenReturn(t);
        when(t.render(Locale.US)).thenReturn(b);
        Mockito.doAnswer(new Answer<Object>()
        {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                WikiPrinter printer = (WikiPrinter) invocation.getArguments()[1];
                printer.print("Only features similar to a reference patient are accessible.");
                return null;
            }
        }).when(r).render(same(b), any(WikiPrinter.class));
        Assert.assertEquals("Only features similar to a reference patient are accessible.",
            this.mocker.getComponentUnderTest().getDescription());
    }

    /** {@link AccessLevel#getDescription()} returns an empty string when a translation isn't found. */
    @Test
    public void getDescriptionWithoutTranslation() throws ComponentLookupException
    {
        Assert.assertEquals("", this.mocker.getComponentUnderTest().getDescription());
    }

    /** Basic test for {@link AccessLevel#toString()}. */
    @Test
    public void toStringTest() throws ComponentLookupException
    {
        Assert.assertEquals("match", this.mocker.getComponentUnderTest().toString());
    }

    /** Basic test for {@link AccessLevel#equals(Object)}. */
    @Test
    public void equalsTest() throws ComponentLookupException
    {
        // Equals itself
        Assert.assertTrue(this.mocker.getComponentUnderTest().equals(this.mocker.getComponentUnderTest()));
        // Never equals null
        Assert.assertFalse(this.mocker.getComponentUnderTest().equals(null));
        // Equals another level with the same name
        AccessLevel other = mock(AccessLevel.class);
        when(other.getName()).thenReturn("match", "view");
        Assert.assertTrue(this.mocker.getComponentUnderTest().equals(other));
        // Doesn't equal a level with a different name
        Assert.assertFalse(this.mocker.getComponentUnderTest().equals(other));
        // Doesn't equal other types of objects
        Assert.assertFalse(this.mocker.getComponentUnderTest().equals("match"));
    }

    /** Basic test for {@link AccessLevel#compareTo(AccessLevel)}. */
    @Test
    public void compareToTest() throws ComponentLookupException
    {
        // Equals itself
        Assert.assertEquals(0, this.mocker.getComponentUnderTest().compareTo(this.mocker.getComponentUnderTest()));
        // Nulls come after
        Assert.assertTrue(this.mocker.getComponentUnderTest().compareTo(null) < 0);
        // Equals another level with the same permissiveness
        Assert.assertEquals(0, this.mocker.getComponentUnderTest().compareTo(new MockAccessLevel("partial", 5, true)));
        // Respects the permissiveness order
        Assert.assertTrue(this.mocker.getComponentUnderTest().compareTo(new MockAccessLevel("private", 0, true)) > 0);
        Assert.assertTrue(this.mocker.getComponentUnderTest().compareTo(new MockAccessLevel("manage", 30, true)) < 0);
        // Other types of levels are placed after
        Assert.assertTrue(this.mocker.getComponentUnderTest().compareTo(mock(AccessLevel.class)) < 0);
    }

    /** Basic tests for {@link AccessLevel#hashCode()}. */
    @Test
    public void hashCodeTest() throws ComponentLookupException
    {
        AccessLevel edit = this.mocker.getComponentUnderTest();
        AccessLevel other = new MockAccessLevel("match", 120, false);
        // Same hashcode for a different access level with the same name and assignable flag, ignoring permissiveness
        Assert.assertEquals(edit.hashCode(), other.hashCode());
        // Different hashcodes for different coordinates
        other = new MockAccessLevel("view", 5, false);
        Assert.assertNotEquals(edit.hashCode(), other.hashCode());
        other = new MockAccessLevel("match", 5, true);
        Assert.assertNotEquals(edit.hashCode(), other.hashCode());
        other = new MockAccessLevel("none", 5, true);
        Assert.assertNotEquals(edit.hashCode(), other.hashCode());
    }
}
