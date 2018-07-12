package com.example.nidhij1.parsetagram;

import android.arch.paging.DataSource;

import com.example.nidhij1.parsetagram.model.Post;

public class ParseDataSourceFactory extends DataSource.Factory<Integer, Post> {

    @Override
    public DataSource<Integer, Post> create() {
        ParsePositionalDataSource source = new ParsePositionalDataSource();
        return source;
    }
}