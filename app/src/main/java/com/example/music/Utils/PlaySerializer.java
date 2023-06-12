package com.example.music.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PlaySerializer {
    public static int NO_MODE = -1;
    public static int REPEAT_ALL = 0;
    public static int REPEAT_ONE = 1;
    public static int SHUFFLE = 2;
    static PlaySerializer instance;
    ArrayList<File> musicList;
    Integer musicListSize = null;
    private ArrayList<shuffledIndex> shuffledMusicList = new ArrayList<>();
    private ArrayList<primaryIndex> primaryMusicList = new ArrayList<>();
    private int selectedIndex = 0;
    private int virtualIndex;

    PlaySerializer() {
    }

    public static PlaySerializer getInstance() {
        if (instance == null) {
            instance = new PlaySerializer();
        }
        return instance;
    }

    public void setMusicList(ArrayList<File> musicList) {
        shuffledMusicList.clear();
        primaryMusicList.clear();
        this.musicList = musicList;
        this.musicListSize = musicList.size();
        for (int index = 0; index < musicList.size(); index++) {
            shuffledMusicList.add(new shuffledIndex(musicList.get(index), index));
        }
        Collections.shuffle(shuffledMusicList);
        for(int i = 0; i<musicListSize; i++) {
            primaryMusicList.add(i,new primaryIndex());
        }
        for (int index = 0; index < musicList.size(); index++) {
            primaryMusicList.set(shuffledMusicList.get(index).primaryIndex,
                    new primaryIndex(shuffledMusicList.get(index).file, index));
        }
        System.out.println(primaryMusicList.toString());
    }

    public File getMusicFile(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        File file = null;
        if(musicListSize != null) {
            if (primaryMusicList.size() > selectedIndex && selectedIndex >= 0) {
                file = primaryMusicList.get(selectedIndex).file;
                this.virtualIndex = primaryMusicList.get(selectedIndex).shuffledIndex;
            } else {
                file = primaryMusicList.get(0).file;
                this.virtualIndex = primaryMusicList.get(0).shuffledIndex;
            }
        }
        return file;
    }

    public File getNextMusicFile(int playMode) {
        File file = null;
        //Empty list check
        if(musicListSize == null || musicListSize == 0) {
            System.out.println("No Music List Assigned.");
            return null;
        }
        if (playMode == REPEAT_ALL) {
            selectedIndex++;
            if(selectedIndex >= musicListSize) {
                selectedIndex = 0;
            }
            file = primaryMusicList.get(selectedIndex).file;
            virtualIndex = primaryMusicList.get(selectedIndex).shuffledIndex;
        } else if (playMode == REPEAT_ONE) {
            file = primaryMusicList.get(selectedIndex).file;
            virtualIndex = primaryMusicList.get(selectedIndex).shuffledIndex;
        } else if (playMode == SHUFFLE) {
            virtualIndex++;
            if(virtualIndex >= musicListSize) {
                virtualIndex = 0;
            }
            file = shuffledMusicList.get(virtualIndex).file;
            selectedIndex = shuffledMusicList.get(virtualIndex).primaryIndex;
        } else {
            file = primaryMusicList.get(selectedIndex).file;
            virtualIndex = primaryMusicList.get(selectedIndex).shuffledIndex;
        }
        return file;
    }

    public File getPreviousMusicFile(int playMode) {
        File file = null;
        //Empty list check
        if(musicListSize == null || musicListSize == 0) {
            System.out.println("No Music List Assigned.");
            return null;
        }
        if (playMode == REPEAT_ALL) {
            selectedIndex--;
            if(selectedIndex < 0) {
                selectedIndex = musicListSize-1;
            }
            file = primaryMusicList.get(selectedIndex).file;
            virtualIndex = primaryMusicList.get(selectedIndex).shuffledIndex;
        } else if (playMode == REPEAT_ONE) {
            file = primaryMusicList.get(selectedIndex).file;
            virtualIndex = primaryMusicList.get(selectedIndex).shuffledIndex;
        } else if (playMode == SHUFFLE) {
            virtualIndex--;
            if(virtualIndex < 0) {
                virtualIndex = musicListSize;
            }
            file = shuffledMusicList.get(virtualIndex).file;
            selectedIndex = shuffledMusicList.get(virtualIndex).primaryIndex;
        } else {
            file = primaryMusicList.get(selectedIndex).file;
            virtualIndex = primaryMusicList.get(selectedIndex).shuffledIndex;
        }
        return file;
    }
    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
        virtualIndex = primaryMusicList.get(selectedIndex).shuffledIndex;
    }
    public int getSelectedIndex() {
        return selectedIndex;
    }

}

class primaryIndex {
    public File file;
    public int shuffledIndex;

    public primaryIndex(File file, int shuffledIndex) {
        this.file = file;
        this.shuffledIndex = shuffledIndex;
    }

    public primaryIndex() {
    }
}

class shuffledIndex {
    public File file;
    public int primaryIndex;

    public shuffledIndex(File file, int primaryIndex) {
        this.file = file;
        this.primaryIndex = primaryIndex;
    }

    public shuffledIndex() {
    }
}