package io.github.stemlab.manage;

import io.github.stemlab.data.Index;

import java.util.HashSet;
import java.util.List;

public interface Manager {

    void makeStructure(String path);

    List<HashSet<String>> findResult(String path);

    Index getIndex();
}
