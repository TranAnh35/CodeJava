package Snake;

import java.awt.*;
import java.util.ArrayList;

public class GameMap {
    private List<String> map;
    private static final int ROWS = 15;
    private static final int COLS = 26;

    public GameMap() {
        map = new ArrayList<>();
        // Thiết lập các giá trị ban đầu cho map
        // 0: ô trống, 1: rắn, 2: thức ăn
        // Ví dụ: Ta sẽ vẽ một hình chữ "U" làm map đơn giản
        // for (int i = 0; i < ROWS; i++) {
        //     for (int j = 0; j < COLS; j++) {
        //         if (i == ROWS - 1 || (j == 0 || j == COLS - 1)) {
        //             map[i][j] = 1; // Đánh dấu rắn
        //         } else {
        //             map[i][j] = 0; // Ô trống
        //         }
        //     }
        // }
        map.add("111111111111111");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("100000000000001");
        map.add("111111111111111");;
    }
    public void draw(Graphics g, int unitSize) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (map[i][j] == 1) {
                    g.setColor(Color.GREEN);
                } else if (map[i][j] == 2) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * unitSize, i * unitSize, unitSize, unitSize);
            }
        }
    }
}
