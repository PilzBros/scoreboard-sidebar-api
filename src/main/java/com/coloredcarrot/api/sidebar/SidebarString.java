/*
 * MIT License
 *
 * Copyright (c) 2018 ColoredCarrot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.coloredcarrot.api.sidebar;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * All rights reserved.
 *
 * @author ColoredCarrot
 * @since 2.0
 */
public class SidebarString implements ConfigurationSerializable {

	/*
	 * Code by fren_gor && ColoredCarrot
	 */
	/**
	 * Generates a scrolling animation for the text with stable prefix and
	 * suffix. For example, this:
	 * {@code generateScrollingAnimation("Hello","-> ", " <-", 10)} will
	 * generate a SidebarString with the following variations: "-> He <-", "->
	 * el <-", "-> ll <-", "-> lo <-"
	 *
	 * @param text
	 *            (String) - the text
	 * @param prefix
	 *            (String) - the prefix of the scrolling text
	 * @param suffix
	 *            (String) - the suffix of the scrolling text
	 * @param displayWidth
	 *            (int) - how many letters to fit into one animation
	 * @return (SidebarString) - the generated SidebarString, ready for use in a
	 *         Sidebar.
	 */
	public static SidebarString generateScrollingAnimation(String text, String prefix, String suffix,
			int displayWidth) {

		if (text.length() <= displayWidth)
			return new SidebarString(text);

		SidebarString sidebarString = new SidebarString();

		for (int i = 0; i <= text.length() - displayWidth; i++) {
			String pre = text.substring(0, i);
			String suff = text.substring(i, displayWidth + i);
			if (suff.endsWith("�")) {
				suff = suff.substring(0, suff.length() - 1);
			}
			if (pre.endsWith("�")) {
				pre.substring(0, pre.length() - 1);
				sidebarString.addVariation("�" + suff);
				continue;
			}
			String color = LongSidebar.getLastChatColor(pre);
			sidebarString.addVariation(prefix + color + suff + suffix);
			if (text.charAt(i) == '�') {
				i += 2;
			} else if (text.charAt(i) == ' ') {
				i++;
			}
		}
		return sidebarString;

	}

	/*
	 * Code by fren_gor
	 */
	/**
	 * Generates a scrolling animation for the text. For example, this:
	 * {@code generateScrollingAnimation("Hello", 10)} will generate a
	 * SidebarString with the following variations: "He", "el", "ll", "lo"
	 *
	 * @param text
	 *            (String) - the text
	 * @param displayWidth
	 *            (int) - how many letters to fit into one animation
	 * @return (SidebarString) - the generated SidebarString, ready for use in a
	 *         Sidebar.
	 */
	public static SidebarString generateScrollingAnimation(String text, int displayWidth) {

		return generateScrollingAnimation(text, "", "", displayWidth);

	}

	static {
		ConfigurationSerialization.registerClass(SidebarString.class);
	}

	private List<String> animated = new ArrayList<>();
	private transient int i = 0, curStep;
	/**
	 * @since 2.8
	 */
	private int step = 1;

	/**
	 * Constructs a new SidebarString.
	 *
	 * @param variations
	 *            (String...) - the variations (for animated text)
	 */
	public SidebarString(String... variations) {

		if (variations != null && variations.length > 0)
			animated.addAll(Arrays.asList(variations));

		curStep = step;

	}

	/**
	 * Constructs a new SidebarString.
	 *
	 * @param step
	 *            (int) - see {@link #setStep(int)}
	 * @param variations
	 *            (String...) - the variations (for animated text)
	 * @since 2.8
	 */
	public SidebarString(int step, String... variations) {

		if (step <= 0)
			throw new IllegalArgumentException("step cannot be smaller than or equal to 0!");

		this.step = step;

		if (variations != null && variations.length > 0)
			animated.addAll(Arrays.asList(variations));

		curStep = step;

	}

	/**
	 * Constructs a new SidebarString. If setPlaceholdersForPlayer is not null,
	 * the placeholders for the variations will be set.
	 *
	 * @param setPlaceholdersForPlayer
	 *            (Player) - what player to set the placeholders for
	 * @param variations
	 *            (String...) - the variations (for animated text) (may be null)
	 * @throws SidebarOptionalException
	 *             if the PlaceholderAPI is not hooked.
	 * @since 2.4
	 */
	public SidebarString(Player setPlaceholdersForPlayer, String... variations) throws SidebarOptionalException {

		addVariation(setPlaceholdersForPlayer, variations);

		curStep = step;

	}

	/**
	 * Constructs a new SidebarString. If setPlaceholdersForPlayer is not null,
	 * the placeholders for the variations will be set.
	 *
	 * @param setPlaceholdersForPlayer
	 *            (Player) - what player to set the placeholders for
	 * @param step
	 *            (int) - see {@link #setStep(int)}
	 * @param variations
	 *            (String...) - the variations (for animated text) (may be null)
	 * @throws SidebarOptionalException
	 *             if the PlaceholderAPI is not hooked.
	 * @since 2.8
	 */
	public SidebarString(Player setPlaceholdersForPlayer, int step, String... variations)
			throws SidebarOptionalException {

		if (step <= 0)
			throw new IllegalArgumentException("step cannot be smaller than or equal to 0!");

		addVariation(setPlaceholdersForPlayer, variations);

		this.step = step;

		curStep = step;

	}

	@SuppressWarnings("unchecked")
	public SidebarString(Map<String, Object> map) {

		animated = (List<String>) map.get("data");

		try {
			step = map.get("step") == null ? 0 : (Integer) map.get("step");
		} catch (ClassCastException | NullPointerException e) {
			step = 0;
		}

	}

	@Override
	public Map<String, Object> serialize() {

		Map<String, Object> map = new HashMap<>();

		map.put("data", animated);
		map.put("step", step);

		return map;

	}

	public SidebarString cleanVariations(Player p) {

		// say this:
		// "�7hel"
		// "7hell"
		// "hello"
		// "ello "
		// "llo �"
		// "lo �c"
		// "o �cg"
		// " �cgu"

		List<String> newAnimated = new ArrayList<>();
		boolean lastStartedWithColorChar = false;

		for (String var : animated) {

			if (var.startsWith("�") && lastStartedWithColorChar) {
				newAnimated.add(var);
				lastStartedWithColorChar = true;
			} else if (var.startsWith("�"))
				lastStartedWithColorChar = true;
			else if (lastStartedWithColorChar)
				lastStartedWithColorChar = false;
			else
				newAnimated.add(var);

		}

		animated = newAnimated;

		return this;

	}

	/**
	 * If the PlaceholderAPI is hooked, sets the placeholders of all variants in
	 * this SidebarString.
	 *
	 * @param forPlayer
	 *            (Player) - what player to set the placeholders for
	 * @return (SidebarString) - this SidebarString Object, for chaining.
	 * @throws SidebarOptionalException
	 *             if the PlaceholderAPI is not hooked.
	 * @since 2.4
	 */
	public SidebarString setPlaceholders(Player forPlayer) throws SidebarOptionalException {


		return this;

	}

	/**
	 * Gets the text that comes after the last one, for animated text. This
	 * method only returns the next variant if the step permits it; which is
	 * always by default.
	 *
	 * @return (String) - the next text.
	 */
	public String getNext() {

		if (curStep == step)
			i++;

		curStep++;

		if (curStep > step)
			curStep = 0;

		if (i > animated.size())
			i = 1;

		return animated.get(i - 1);

	}

	/*
	 * Code by fren_gor && ColoredCarrot
	 */
	/**
	 * Calls {@link #getNext()} and, if necessary, trims the result to max. 28
	 * characters and prints a warning message to the specified logger.
	 * 
	 * @param logger
	 *            The logger to print the warning message to
	 * @return The next text
	 * @see #getNext()
	 */
	public String getNextAndTrim(Logger logger, boolean isLongText) {
		String next = getNext();
		if (next.startsWith("�r") || next.startsWith("�f"))
			next = next.substring(2);

		if (next.startsWith("�r�f") || next.startsWith("�f�r"))
			next = next.substring(4);
		if (isLongText) {
			if (next.length() > 64) {
				logger.warning("[Sidebar] Entry variation #" + (i + 1) + " was trimmed to 64 characters (originally \""
						+ next + "\")");
				next = next.substring(0, 64);
				if (next.endsWith("�")) {
					next = next.substring(0, 63);
				}
				animated.set(i - 1, next);
			}
		} else {
			if (next.length() > 28) {
				logger.warning("[Sidebar] Entry variation #" + (i + 1) + " was trimmed to 28 characters (originally \""
						+ next + "\")");
				next = next.substring(0, 28);
				if (next.endsWith("�")) {
					next = next.substring(0, 27);
				}
				animated.set(i - 1, next);
			}
		}
		return next;
	}

	/**
	 * Resets the animation to the starting point. Since 2.8, this also resets
	 * the current step value so the next call of {@link #getNext()} returns the
	 * next variation.
	 *
	 * @return (SidebarString) - this SidebarString Object, for chaining.
	 */
	public SidebarString reset() {
		i = 0;
		curStep = step;
		return this;
	}

	/**
	 * Returns the step that is currently active.
	 *
	 * @return (SidebarString) - this SidebarString Object, for chaining.
	 * @see #setStep(int)
	 * @since 2.8
	 */
	public int getStep() {
		return step;
	}

	/**
	 * Sets the step of this SidebarString. The "step" defines how many times
	 * the method {@link #getNext()} needs to be run before the actual new
	 * variant will be returned.
	 *
	 * @param step
	 *            (int) - the step, must be > 0
	 * @return (SidebarString) - this SidebarString Object, for chaining.
	 * @since 2.8
	 */
	public SidebarString setStep(int step) {

		if (step <= 0)
			throw new IllegalArgumentException("step cannot be smaller than or equal to 0!");

		this.step = step;

		curStep = step;

		return this;

	}

	/**
	 * Gets all variations of this text.
	 *
	 * @return (List : String) - all animations.
	 */
	public List<String> getVariations() {
		return animated;
	}

	/**
	 * Adds a variation.
	 *
	 * @param variations
	 *            (String...) - the variations to add
	 * @return (SidebarString) - this SidebarString Object, for chaining.
	 */
	public SidebarString addVariation(String... variations) {
		animated.addAll(Arrays.asList(variations));
		return this;
	}

	/**
	 * Adds a variation. If setPlaceholdersForPlayer is not null, the
	 * placeholders will be set for that player in the variation.
	 *
	 * @param setPlaceholdersForPlayer
	 *            (Player) - what player to set the placeholders for (may be
	 *            null)
	 * @param variations
	 *            (String...) - the variation(s) to add
	 * @return (SidebarString) - this SidebarString Object, for chaining.
	 * @since 2.4
	 */
	public SidebarString addVariation(Player setPlaceholdersForPlayer, String... variations) {



		return this;

	}

	/**
	 * Removes a variation.
	 *
	 * @param variation
	 *            (String) - the variation
	 * @return (SidebarString) - this SidebarString Object, for chaining.
	 */
	public SidebarString removeVariation(String variation) {
		animated.remove(variation);
		return this;
	}

}
