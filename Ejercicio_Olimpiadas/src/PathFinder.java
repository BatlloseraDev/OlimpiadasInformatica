import java.awt.*;
import java.util.*;
import java.util.List;

class PathFinder { //Como el juego de rol

    static class Node {
        int x, y, dist;
        boolean isHorizontal;
        List<Point> path;

        Node(int x, int y, int dist, boolean isHorizontal, List<Point> path) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.isHorizontal = isHorizontal;
            this.path = new ArrayList<>(path);
            this.path.add(new Point(x, y));
        }
    }

    public static List<Point> encontrarElCaminoMasCorto(Celda[][] grid, int startX, int startY, int endX, int endY) {
        int rows = grid.length, cols = grid[0].length;
        boolean[][][] visited = new boolean[rows][cols][2];
        int[][] horizontalMoves = {{0, -1}, {0, 1}};
        int[][] verticalMoves = {{-1, 0}, {1, 0}};

        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.dist)); // Cola de prioridad por distancia
        boolean isHorizontal = grid[startX][startY].get_TipoCarretera() == 'h'; //saca si es horizontal o vertical al inicio
        queue.add(new Node(startX, startY, 0, isHorizontal, new ArrayList<>()));
        visited[startX][startY][1] = true; //para no volver a pasar dos veces por el mismo sitio

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            int x = current.x, y = current.y;

            if (x == endX && y == endY) {
                return current.path; //devuelve el camino
            }

            int[][] moves = current.isHorizontal ? horizontalMoves : verticalMoves; //compara si es horizontal o vertical

            for (int[] dir : moves) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (esValido(grid, newX, newY, rows, cols) && !visited[newX][newY][current.isHorizontal ? 1 : 0]) {
                    visited[newX][newY][current.isHorizontal ? 1 : 0] = true;
                    queue.add(new Node(newX, newY, current.dist + 1, current.isHorizontal, current.path));

                    if(grid[newX][newY].get_Cruce()){
                        queue.add(new Node(newX, newY, current.dist + 1, !current.isHorizontal, current.path));
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private static boolean esValido(Celda[][] grid, int x, int y, int rows, int cols) {
        return x >= 0 && x < rows && y >= 0 && y < cols && grid[x][y].get_Carretera();
    }
}
