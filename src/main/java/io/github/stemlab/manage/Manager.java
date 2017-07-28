package io.github.stemlab.manage;

import io.github.stemlab.data.Index;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public interface Manager {

    void makeStructure(String path);

    //Method created for test
    List<HashSet<String>> processQueryAndGetResult(String path) throws IOException;

    void processQuery(String path) throws IOException;

    Index getIndex();
}
