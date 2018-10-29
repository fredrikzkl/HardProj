import java.util.LinkedHashMap;

class TrieNode {
    private TrieNode[] children = new TrieNode[128];
    private LinkedHashMap<String, Boolean> prefixes;

    private TrieNode() {
    }

    TrieNode(LinkedHashMap<String, Boolean> prefixes) {
        this.prefixes = prefixes;
    }

    void insertString(String s) {
        TrieNode v = this;
        for (char ch : s.toCharArray()) {
            TrieNode next = v.children[ch];
            if (next == null) v.children[ch] = next = new TrieNode();
            v = next;
        }
    }

    boolean prefix(String prefix) {
        if (prefixes.containsKey(prefix)) {
            return prefixes.get(prefix);
        }
        boolean result = this.prefix(prefix, prefix.length());
        prefixes.put(prefix, result);
        return result;
    }

    private boolean prefix(String prefix, int length) {
        if (length == 0) return true;
        TrieNode child = this.children[prefix.charAt(0)];
        if (child != null) return child.prefix(prefix.substring(1), length-1);
        return false;
    }
}