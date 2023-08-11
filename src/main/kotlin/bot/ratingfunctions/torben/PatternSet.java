package bot.ratingfunctions.torben;

import java.util.ArrayList;
import java.util.List;

public class PatternSet {
    private List<Pattern> patterns = new ArrayList<>();
    
    public PatternSet() {
        init();
    }
    
    private void init() {
        String[][] patternStrings = {
            {"xxxx"},
            {"x","x","x","x"},
            {"----", "x---", "0x--", "00x-", "000x"},
            {"----", "---x", "--x0", "-x00", "x000"},
            {"----", "xxx "},
            {"--- ", "xxx "},
            {"----", "xx x"},
            {"-- -", "xx x"},
            {"----", "x xx"},
            {"- --", "x xx"},
            {"----", " xxx"},
            {" ---", " xxx"},
            {"----", "xx  "},
            {"--- ", "xx  "},
            {"-- -", "xx  "},
            {"--  ", "xx  "},
            {"----", "x x "},
            {"--- ", "x x "},
            {"- --", "x x "},
            {"- - ", "x x "},
            {"----", "x  x"},
            {"-- -", "x  x"},
            {"- --", "x  x"},
            {"-  -", "x  x"},
            {"----", " xx "},
            {"--- ", " xx "},
            {" ---", " xx "},
            {" -- ", " xx "},
            {"----", " x x"},
            {"-- -", " x x"},
            {" ---", " x x"},
            {" - -", " x x"},
            {"----", "  xx"},
            {"- --", "  xx"},
            {" ---", "  xx"},
            {"  --", "  xx"},
            {"----", "x   "},
            {"- --", "x   "},
            {"-- -", "x   "},
            {"--- ", "x   "},
            {"-  -", "x   "},
            {"- - ", "x   "},
            {"--  ", "x   "},
            {"-   ", "x   "},
            {"----", " x  "},
            {" ---", " x  "},
            {"-- -", " x  "},
            {"--- ", " x  "},
            {" - -", " x  "},
            {" -- ", " x  "},
            {"--  ", " x  "},
            {" -  ", " x  "},
            {"----", "  x "},
            {" ---", "  x "},
            {"- --", "  x "},
            {"--- ", "  x "},
            {"  --", "  x "},
            {" -- ", "  x "},
            {"- - ", "  x "},
            {"  - ", "  x "},
            {"----", "   x"},
            {" ---", "   x"},
            {"- --", "   x"},
            {"-- -", "   x"},
            {"  --", "   x"},
            {" - -", "   x"},
            {"-  -", "   x"},
            {"   -", "   x"},
            {"x","x","x"," "},
            {"x","x"," "," "},
            {"x"," "," "," "},
            {"----", " ---", "0x--", "00x-", "000x"},
            {" ---", " ---", "0x--", "00x-", "000x"},

            {"----", "x---", "0 --", "00x-", "000x"},
            {"----", "x --", "0 --", "00x-", "000x"},

            {"----", "x---", "0x--", "00 -", "000x"},
            {"----", "x-0-", "0x -", "00 -", "000x"},

            {"----", "x---", "0x--", "00x-", "000 "},
            {"----", "x--0", "0x-0", "00x ", "000 "},

            {"----", "x---", "0x--", "00 -", "000 "},
            {"----", "x-0-", "0x -", "00 -", "000 "},
            {"----", "x--0", "0x-0", "00  ", "000 "},
            {"----", "x-00", "0x 0", "00  ", "000 "},

            {"----", "x---", "0 --", "00x-", "000 "},
            {"----", "x --", "0 --", "00x-", "000 "},
            {"----", "x--0", "0 -0", "00x ", "000 "},
            {"----", "x -0", "0 -0", "00x ", "000 "},

            {"----", "x---", "0 --", "00 -", "000x"},
            {"----", "x --", "0 --", "00 -", "000x"},
            {"----", "x-0-", "0  -", "00 -", "000x"},
            {"----", "x 0-", "0  -", "00 -", "000x"},

            {"----", " ---", " x--", "00x-", "000 "},
            {" ---", " ---", " x--", "00x-", "000 "},
            {"----", " --0", " x-0", "00x ", "000 "},
            {" --0", " --0", " x-0", "00x ", "000 "},

            {"----", " ---", " x--", "00 -", "000x"},
            {" ---", " ---", " x--", "00 -", "000x"},
            {"----", " -0-", " x -", "00 -", "000x"},
            {" -0-", " -0-", " x -", "00 -", "000x"},

            {"----", " ---", "  --", "00x-", "000x"},
            {" ---", " ---", "  --", "00x-", "000x"},
            {"----", "  --", "  --", "00x-", "000x"},
            {" 0--", "  --", "  --", "00x-", "000x"},

            {"----", "x---", "0 --", "00 -", "000 "},
            {"----", "x--0", "0 -0", "00  ", "000 "},
            {"----", "x-0-", "0  -", "00 -", "000 "},
            {"----", "x-00", "0  0", "00  ", "000 "},
            {"----", "x --", "0 --", "00 -", "000 "},
            {"----", "x -0", "0 -0", "00  ", "000 "},
            {"----", "x 0-", "0  -", "00 -", "000 "},
            {"----", "x 00", "0  0", "00  ", "000 "},

            {"----", " ---", "0x--", "00 -", "000 "},
            {"----", " --0", "0x-0", "00  ", "000 "},
            {"----", " -0-", "0x -", "00 -", "000 "},
            {"----", " -00", "0x 0", "00  ", "000 "},
            {" ---", " ---", "0x--", "00 -", "000 "},
            {" --0", " --0", "0x-0", "00  ", "000 "},
            {" -0-", " -0-", "0x -", "00 -", "000 "},
            {" -00", " -00", "0x 0", "00  ", "000 "},

            {"----", " ---", "0 --", "00x-", "000 "},
            {"----", " --0", "0 -0", "00x ", "000 "},
            {"----", "  --", "0 --", "00x-", "000 "},
            {"----", "  -0", "0 -0", "00x ", "000 "},
            {" ---", " ---", "0 --", "00x-", "000 "},
            {" --0", " --0", "0 -0", "00x ", "000 "},
            {" 0--", "  --", "0 --", "00x-", "000 "},
            {" 0-0", "  -0", "0 -0", "00x ", "000 "},

            {"----", " ---", "0 --", "00 -", "000x"},
            {"----", " -0-", "0  -", "00 -", "000x"},
            {"----", "  --", "0 --", "00 -", "000x"},
            {"----", "  0-", "0  -", "00 -", "000x"},
            {" ---", " ---", "0 --", "00 -", "000x"},
            {" -0-", " -0-", "0  -", "00 -", "000x"},
            {" 0--", "  --", "0 --", "00 -", "000x"},
            {" 00-", "  0-", "0  -", "00 -", "000x"},
            {"----", "--- ", "--x0", "-x00", "-x00"},

            {"----", "--- ", "--x0", "-x00", "x000"}, 
            {"--- ", "--- ", "--x0", "-x00", "x000"}, 
            {"----", "---x", "-- 0", "-x00", "x000"}, 
            {"----", "-- x", "-- 0", "-x00", "x000"}, 
            {"----", "---x", "--x0", "- 00", "x000"}, 
            {"----", "-0-x", "- x0", "- 00", "x000"}, 
            {"----", "---x", "--x0", "-x00", " 000"}, 
            {"----", "0--x", "0-x0", " x00", " 000"}, 
            {"----", "---x", "--x0", "- 00", " 000"}, 
            {"----", "-0-x", "- x0", "- 00", " 000"}, 
            {"----", "0--x", "0-x0", "  00", " 000"}, 
            {"----", "00-x", "0 x0", "  00", " 000"}, 
            {"----", "---x", "-- 0", "-x00", " 000"}, 
            {"----", "-- x", "-- 0", "-x00", " 000"}, 
            {"----", "0--x", "0- 0", " x00", " 000"}, 
            {"----", "0- x", "0- 0", " x00", " 000"}, 
            {"----", "---x", "-- 0", "- 00", "x000"}, 
            {"----", "-- x", "-- 0", "- 00", "x000"}, 
            {"----", "-0-x", "-  0", "- 00", "x000"}, 
            {"----", "-0 x", "-  0", "- 00", "x000"}, 
            {"----", "--- ", "--x ", "-x00", " 000"}, 
            {"--- ", "--- ", "--x ", "-x00", " 000"}, 
            {"----", "0-- ", "0-x ", " x00", " 000"}, 
            {"0-- ", "0-- ", "0-x ", " x00", " 000"}, 
            {"----", "--- ", "--x ", "- 00", "x000"}, 
            {"--- ", "--- ", "--x ", "- 00", "x000"}, 
            {"----", "-0- ", "- x ", "- 00", "x000"}, 
            {"-0- ", "-0- ", "- x ", "- 00", "x000"}, 
            {"----", "--- ", "--  ", "-x00", "x000"}, 
            {"--- ", "--- ", "--  ", "-x00", "x000"}, 
            {"----", "--  ", "--  ", "-x00", "x000"}, 
            {"--0 ", "--  ", "--  ", "-x00", "x000"}, 
            {"----", "---x", "-- 0", "- 00", " 000"}, 
            {"----", "0--x", "0- 0", "  00", " 000"}, 
            {"----", "-0-x", "-  0", "- 00", " 000"}, 
            {"----", "00-x", "0  0", "  00", " 000"}, 
            {"----", "-- x", "-- 0", "- 00", " 000"}, 
            {"----", "0- x", "0- 0", "  00", " 000"}, 
            {"----", "-0 x", "-  0", "- 00", " 000"}, 
            {"----", "00 x", "0  0", "  00", " 000"}, 
            {"----", "--- ", "--x0", "- 00", " 000"}, 
            {"----", "0-- ", "0-x0", "  00", " 000"}, 
            {"----", "-0- ", "- x0", "- 00", " 000"}, 
            {"----", "00- ", "0 x0", "  00", " 000"}, 
            {"--- ", "--- ", "--x0", "- 00", " 000"}, 
            {"0-- ", "0-- ", "0-x0", "  00", " 000"}, 
            {"-0- ", "-0- ", "- x0", "- 00", " 000"}, 
            {"00- ", "00- ", "0 x0", "  00", " 000"}, 
            {"----", "--- ", "-- 0", "-x00", " 000"}, 
            {"----", "0-- ", "0- 0", " x00", " 000"}, 
            {"----", "--  ", "-- 0", "-x00", " 000"}, 
            {"----", "0-  ", "0- 0", " x00", " 000"}, 
            {"--- ", "--- ", "-- 0", "-x00", " 000"}, 
            {"0-- ", "0-- ", "0- 0", " x00", " 000"}, 
            {"--0 ", "--  ", "-- 0", "-x00", " 000"}, 
            {"0-0 ", "0-  ", "0- 0", " x00", " 000"}, 
            {"----", "--- ", "-- 0", "- 00", "x000"}, 
            {"----", "-0- ", "-  0", "- 00", "x000"}, 
            {"----", "--  ", "-- 0", "- 00", "x000"}, 
            {"----", "-0  ", "-  0", "- 00", "x000"}, 
            {"--- ", "--- ", "-- 0", "- 00", "x000"}, 
            {"-0- ", "-0- ", "-  0", "- 00", "x000"}, 
            {"--0 ", "--  ", "-- 0", "- 00", "x000"}, 
            {"-00 ", "-0  ", "-  0", "- 00", "x000"}, 
            {"----", " ---", "0x--", "00x-", "00x-"}, 
        };
        for (String[] pat : patternStrings) patterns.add(new Pattern(pat));
        // for (int i = 71; i < patternStrings.length; i++) mirror(patternStrings[i]);
    }

    // private Pattern mirror(String[] strings) {
    //     for (int i = 0; i < strings.length; i++) {
    //         strings[i] = new StringBuilder(strings[i]).reverse().toString();
    //     }
    //     System.out.println("{\"" + strings[0] + "\", \"" + strings[1] + "\", \"" + strings[2] + "\", \"" + strings[3] + "\", \"" + strings[4] + "\"}, ");
    //     return new Pattern(strings);
    // }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer().append("---------------\n");
        for (Pattern pattern : patterns) str.append(pattern.toString()).append("\n---------------\n");
        return str.toString();
    }

    public int getPatternAmount() {
        return patterns.size();
    }

    public Pattern getPattern(int index) {
        return patterns.get(index);
    }
    
    // private void inits() {
    //     int[][][] init = {
    //         //4 threats
    //         // x-x-x-x
    //         {{Pattern.ME}, {Pattern.ME}, {Pattern.ME}, {Pattern.ME}},
            
    //         //3 threats
    //         // x-_-x-x
    //         {{Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}},
    //         {{Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}},
    //         // x-x-_-x
    //         {{Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}},
    //         {{Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}},
    //         // _-x-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}},
    //         // _-x-x-x-o
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         // o-x-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}},
            
    //         //2 threats
    //         // x-_-x
    //         {{Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}},
    //         {{Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}},
    //         // _-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}},
    //         // _-x-x-o
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         // o-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}},

    //         //1 threats
    //         // _-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}},
    //         // _-x-o
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         // o-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.EMPTY, Pattern.EMPTY}},



    //         //-----------------------------------SENKRECHT--------------------------------------------------------


    //         //4 threats
    //         // x-x-x-x
    //         {{Pattern.ME, Pattern.ME, Pattern.ME, Pattern.ME}},

    //         //3 threats
    //         // o-x-x-x-_
    //         {{Pattern.ME, Pattern.ME, Pattern.ME, Pattern.EMPTY}},
            
    //         //2 threats
    //         // o-x-x-_
    //         {{Pattern.ME, Pattern.ME, Pattern.EMPTY}},

    //         //1 threats
    //         // o-x-_
    //         {{Pattern.ME, Pattern.EMPTY}},


    //         //-------------------------------DIAGONAL /--------------------------------------------------------


    //         //0 - 4 threats
    //         {{Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}},

    //         //1 - 12 3 threats
    //         // x-_-x-x
    //         {{Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}},
    //         {{Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}},
    //         // x-x-_-x
    //         {{Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}},
    //         {{Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}},
    //         // _-x-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}},
    //         // _-x-x-x-o
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         // o-x-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}},

            
    //         //13 - 22 2 threats
    //         // x-_-x
    //         {{Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}},
    //         {{Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}},
    //         // _-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}},
    //         // _-x-x-o
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         // o-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}},

    //         //23 - 30 1 threats
    //         // _-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}},
    //         // _-x-o
    //         {{Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         {{Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}},
    //         // o-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}},
    //         {{Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}},


    //         //---------------------------------------------------------------------DIAGONAL \ ----------------------------------------------------------------------------------------


    //         //31 4 threats
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}},

    //         //3 threats
    //         // 32 - 43 x-_-x-x
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         // x-x-_-x
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         // _-x-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         // _-x-x-x-o
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         // o-x-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.NULL}},

            
    //         //2 threats
    //         // x-_-x
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME}, {Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}},
    //         // _-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         // _-x-x-o
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         // o-x-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NULL, Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL, Pattern.NULL}},

    //         //1 threats
    //         // _-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}},
    //         // _-x-o
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ENEMY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.EMPTY, Pattern.EMPTY, Pattern.NULL, Pattern.NULL}},
    //         // o-x-_
    //         {{Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL}},
    //         {{Pattern.NULL, Pattern.NULL, Pattern.EMPTY, Pattern.EMPTY}, {Pattern.NOT_EMPTY, Pattern.NOT_EMPTY, Pattern.ME, Pattern.NULL}, {Pattern.NOT_EMPTY, Pattern.ENEMY, Pattern.NULL, Pattern.NULL}},



    //     };
    //     for (int[][] pat : init) patterns.add(new Pattern(pat));
    // }


}
