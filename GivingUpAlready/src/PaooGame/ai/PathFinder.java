package PaooGame.ai;

import PaooGame.Game;
import PaooGame.entity.Entity;

import java.util.ArrayList;

public class PathFinder {
    Game game;
    Node[][] nodes;
    public int dim;

    ArrayList<Node> openNodes = new ArrayList<>();
    public ArrayList<Node> path = new ArrayList<>();
    Node startNode,goalNode, currentNode;
    boolean goalReached = false;
    int step=0;

    public PathFinder(Game game, int dim) {
        this.game = game;
        this.dim=dim;
        initNodes();
    }

    public void initNodes() {
        nodes = new Node[dim][dim];

        int col=0;int row=0;

        while( col < dim && row < dim) {
            nodes[row][col] = new Node(row,col);

            col++;if(col==dim) {col=0;row++;}
        }
    }

    public void resetNodes() {
        int col=0;int row=0;

        while( col < dim && row < dim) {
           nodes[row][col].open =false;
           nodes[row][col].checked = false;
           nodes[row][col].solid=false;

            col++;if(col==dim) {col=0;row++;}
        }

        openNodes.clear();
        path.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow, Entity entity) {
        resetNodes();

        startNode = nodes[startRow][startCol];
        currentNode = startNode;
        goalNode = nodes[goalRow][goalCol];
        openNodes.add(currentNode);

        int col=0;int row=0;

        while( col < dim && row < dim) {
            int tileNum = game.tileMan.getTileNum(row,col);
            nodes[row][col].solid = game.tileMan.checkTile(tileNum);

            getCost(nodes[row][col]);

            col++;if(col==dim) {col=0;row++;}
        }
    }

    public void getCost(Node node) {
        // G cost
        int xDist = Math.abs(node.col - startNode.col);
        int yDist = Math.abs(node.row - startNode.row);
        node.gCost = xDist + yDist;

        // H cost
        xDist = Math.abs(node.col - goalNode.col);
        yDist = Math.abs(node.row - goalNode.row);
        node.hCost = xDist + yDist;

        // F cost
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {
        while(!goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.checked=true;
            openNodes.remove(currentNode);

            if(row-1 >= 0) {
                openNode(nodes[row-1][col]);
            }
            if(col-1 >= 0) {
                openNode(nodes[row][col-1]);
            }
            if(row+1 < dim) {
                openNode(nodes[row+1][col]);
            }
            if(col+1 < dim) {
                openNode(nodes[row][col+1]);
            }

            int bestIndex = 0;
            int bestCost = 9999;

            for ( int i=0;i<openNodes.size();i++) {
                if(openNodes.get(i).fCost < bestCost) {
                    bestIndex = i;
                    bestCost = openNodes.get(i).fCost;
                }
                else if ( openNodes.get(i).fCost == bestCost ) {
                    if(openNodes.get(i).gCost < openNodes.get(bestIndex).gCost) {
                        bestIndex = i;
                    }
                }
            }

            // no node in the open list => end the loop
            if(openNodes.isEmpty()) {
                break;
            }

            //After the loop, openNodes[bestIndex] is the next step ( = currentNode )
            currentNode = openNodes.get(bestIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }

    public void openNode(Node node) {
        if(!node.open && !node.checked && !node.solid) {
            node.open=true;
            node.parent=currentNode;
            openNodes.add(node);
        }
    }

    public void trackThePath() {
        Node current = goalNode;
        while(current != startNode) {
            path.add(0,current);
            current=current.parent;
        }
    }
}
