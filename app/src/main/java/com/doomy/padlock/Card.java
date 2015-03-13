/**
 * Copyright (C) 2015 Damien Chazoule
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.doomy.padlock;

public class Card {

    private static int mCount = 1;
    private int ID;
    private String Title;
    private int FirstColor;
    private int SecondColor;
    private String Info;
    private int Logo;

    public Card(String mTitle, int mFirstColor, int mSecondColor, String mInfo, int mLogo) {
        this.ID = Card.mCount++;
        this.Title = mTitle;
        this.FirstColor = mFirstColor;
        this.SecondColor = mSecondColor;
        this.Info = mInfo;
        this.Logo = mLogo;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getFirstColor() {
        return FirstColor;
    }

    public void setFirstColor(int firstColor) {
        FirstColor = firstColor;
    }

    public int getSecondColor() {
        return SecondColor;
    }

    public void setSecondColor(int secondColor) {
        SecondColor = secondColor;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public int getLogo() {
        return Logo;
    }

    public void setLogo(int logo) {
        Logo = logo;
    }
}
