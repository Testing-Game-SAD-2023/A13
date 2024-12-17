package com.g2.Model;

import java.util.Collections;
import java.util.Comparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.g2.Components.PageBuilder;
import com.g2.Model.Player;


public class Classifica {


    private List<Player> players;
    private String search;
    private String sortOrder;
    private int page;
    private int size;
    private int totalPages; //variabile strettamente legata a players, infatti la sua modifica é legata al setplayer.

    public Classifica(String search, String sortOrder, int page, int size) {
        this.search = search;
        this.sortOrder = sortOrder;
        this.page = page;
        this.size = size;
        this.totalPages=0;
        this.players=Collections.emptyList();
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    

    // Getter e Setter
    public List<Player> getPlayers() {
        return this.players;
    }
    //ordina anche i giocatori
    public void setPlayers(List<Player> players) {
        this.players = players;
        this.totalPages=(int) Math.ceil((double) this.players.size() / this.size);
    }

    public String getSearch() {
        return this.search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
