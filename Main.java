import java.util.*;
import java.io.*;


class Matrix {
    private int n;
    private int[][] a;

    public Matrix (int n, int[][] a) {
        this.n = n;
        this.a = a;
    }

    public Matrix (Graph g) {
        this.n = g.getN();
        this.a = new int[n][n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (g.get(i, j) > 0) this.a[i][j] = 1;
                else this.a[i][j] = 0;
            }
        }
    }

    public Matrix(int n) {
        this.n = n;
        this.a = new int[n][n];
        for (int i = 0; i < n; i++) this.a[i][i] = 1;
    }

    public int get(int i, int j) {
        return this.a[i][j];
    }

    public Matrix mult(Matrix o) {
        n = this.n;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    res[i][j] += this.get(i, k) * o.get(k, j);
                }
            }
        }
        return new Matrix(n, res);
    }

    public void print(PrintStream cout) {
        cout.println(this.n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) cout.print(" " + this.get(i, j));
            cout.println();
        }
    }
}

class Graph {
    private int n;
    private int[][] g;
    private int[][] floyd;

    public Graph() {
        this.n = 0;
        this.g = new int[100][100];
    }

    public int getN() {
        return this.n;
    }

    public int get(int i, int j) {
        return this.g[i][j];
    }

    private void calculateFloyd() {
        this.floyd = new int[this.n][this.n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (i == j) this.floyd[i][j] = 0;
                else if (this.g[i][j] > 0) this.floyd[i][j] = this.g[i][j];
                else this.floyd[i][j] = 1000000000;
            }
        }
        for (int k = 0; k < this.n; k++) {
            for (int i = 0; i < this.n; i++) {
                for (int j = 0; j < this.n; j++) {
                    this.floyd[i][j] = Math.min(this.floyd[i][j], this.floyd[i][k] + this.floyd[k][j]);
                }
            }
        }
    }

    public void add(char from, char to, int length) {
        int u = from - 'A';
        int v = to - 'A';
        this.n = Math.max(this.n, Math.max(u + 1, v + 1));
        this.g[u][v] = length;
        this.calculateFloyd();
    }

    public void print(PrintStream cout) {
        cout.println(this.n);
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                cout.print(this.g[i][j] + " ");
            }
            cout.println();
        }
    }

    private int distance(char from, char to) {
        int f = from - 'A';
        int c = to - 'A';
        if (this.g[f][c] == 0) return -1;
        return this.g[f][c];
    }

    public int distance(char[] path) {
        int answer = 0;
        for (int i = 1; i < path.length; i++) {
            int current = this.distance(path[i - 1], path[i]);
            if (current == -1) return -1;
            answer += current;
        }
        return answer;
    }

    public int numberOfWaysAtMost(char from, char to, int steps) {
        Matrix res = new Matrix(this.n);
        int answer = 0;
        for (int i = 0; i < steps; i++) {
            res = res.mult(new Matrix(this));
            int f = from - 'A';
            int c = to - 'A';
            answer += res.get(f, c);
        }
        return answer;
    }

    public int numberOfWaysExact(char from, char to, int steps) {
        Matrix res = new Matrix(this.n);
        int answer = 0;
        for (int i = 0; i < steps; i++) {
            res = res.mult(new Matrix(this));
        }
        int f = from - 'A';
        int c = to - 'A';
        answer += res.get(f, c);
        return answer;
    }

    public int shortestPath(char from, char to) {
        int a = from - 'A';
        int b = to - 'A';
        int result = 1000000000;
        for (int i = 0; i < this.n; i++) {
            if (a == b && a == i) continue;
            result = Math.min(result, this.floyd[a][i] + this.floyd[i][b]);
        }
        if (result != 1000000000) return result;
        return -1;
    }

    public int numberOfWaysLimitByLength(char from, char to, int maxLength) {
        if (maxLength < 0) return 0;
        int result = 0;
        if (from == to) result += 1;
        int f = from - 'A';
        int t = to - 'A';
        for (int i = 0; i < this.n; i++) {
            if (this.get(f, i) > 0) result += this.numberOfWaysLimitByLength((char)(i + 'A'), to, maxLength - this.get(f, i));
        }
        return result;
    }
}

 
public class Main {
	
	public static String s;
	
	public static int t;
	
	public static void main(String args[]) {
        Scanner cin=new Scanner(System.in);

        Graph g = new Graph();

        while (cin.hasNext()) {
            String s = cin.next();
            char c1 = s.charAt(0);
            char c2 = s.charAt(1);
            int length = Integer.parseInt(s.substring(2));
            g.add(c1, c2, length);
        }

        g.print(System.out);

        {
            System.out.print("Output #1: ");
            final char[] path = {'A', 'B', 'C'};
            int result = g.distance(path);
            if (result == -1) System.out.println("NO SUCH ROUTE");
            else System.out.println(result);
        }
        {
            System.out.print("Output #2: ");
            final char[] path = {'A', 'D'};
            int result = g.distance(path);
            if (result == -1) System.out.println("NO SUCH ROUTE");
            else System.out.println(result);
        }
        {
            System.out.print("Output #3: ");
            final char[] path = {'A', 'D', 'C'};
            int result = g.distance(path);
            if (result == -1) System.out.println("NO SUCH ROUTE");
            else System.out.println(result);
        }
        {
            System.out.print("Output #4: ");
            final char[] path = {'A', 'E', 'B', 'C', 'D'};
            int result = g.distance(path);
            if (result == -1) System.out.println("NO SUCH ROUTE");
            else System.out.println(result);
        }
        {
            System.out.print("Output #5: ");
            final char[] path = {'A', 'E', 'D'};
            int result = g.distance(path);
            if (result == -1) System.out.println("NO SUCH ROUTE");
            else System.out.println(result);
        }
        {
            System.out.println("Output #6: " + g.numberOfWaysAtMost('C', 'C', 3));
        }
        {
            System.out.println("Output #7: " + g.numberOfWaysExact('A', 'C', 4));
        }
        {
            System.out.print("Output #8: ");
            int result = g.shortestPath('A', 'C');
            if (result == -1) System.out.println("NO SUCH ROUTE");
            else System.out.println(result);
        }
        {
            System.out.print("Output #9: ");
            int result = g.shortestPath('B', 'B');
            if (result == -1) System.out.println("NO SUCH ROUTE");
            else System.out.println(result);
        }
        {
            System.out.print("Output #10: ");
            int result = g.numberOfWaysLimitByLength('C', 'C', 29);
            if (result > 0) result--;
            System.out.println(result);
        }
	}
}

