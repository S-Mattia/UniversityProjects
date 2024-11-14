package ex4;

import java.util.Collection;

public class prova {

    public static void PrintNode(Collection<String> nodi) {
        if (nodi == null) {
            System.out.println("nessun elemento");
        } else {
            for (String element : nodi) {
                System.out.println(element);
            }
        }

    }

    public static void PrintNodeNeighbours(Graph<String, Integer> grafo) {
        for (String string : grafo.getNodes()) {
            System.out.print("vicini di ");
            System.out.print(string);
            System.out.print(": ");

            Collection<String> edges = grafo.getNeighbours(string);
            if (edges == null) {
                System.out.println("nessun elemento");
            } else {
                for (String element : edges) {
                    System.out.print(element);
                    System.out.print(", ");
                }
            }
            System.out.println("");
        }

    }

    public static void main(String[] args) {
        Graph<String, Integer> grafo1 = new Graph<>(true, false);

        System.out.println("Prima di agigungere nodi...");
        System.out.println("1-------numero nodi ora");
        System.out.println(grafo1.numNodes());
        PrintNode(grafo1.getNodes());
        System.out.println("1-------numero archi ora");
        System.out.println(grafo1.numEdges());

        String parola = "ciao";
        String parola1 = "sono";
        String parola2 = "mattia";
        String parola3 = "girogio";
        String parola4 = "giovanna";
        grafo1.addNode(parola);
        grafo1.addNode(parola1);
        grafo1.addNode(parola2);
        grafo1.addNode(parola3);
        grafo1.addNode(parola4);

        System.out.println("Dopo aver aggiunto tutti i nodi...");
        System.out.println("2-------numero nodi ora");
        System.out.println(grafo1.numNodes());
        PrintNode(grafo1.getNodes());
        System.out.println("2------numero archi ora");
        System.out.println(grafo1.numEdges());
        System.out.println("Prima di aggiungere vicini");
        PrintNodeNeighbours(grafo1);

        grafo1.addEdge(parola, parola3, null);
        grafo1.addEdge(parola, parola4, null);

        System.out.println("3-------numero nodi ora");
        System.out.println(grafo1.numNodes());
        System.out.println("3-------numero archi ora : ");
        System.out.println(grafo1.numEdges());
        System.out.println("dopo di aggiungere vicini");
        PrintNodeNeighbours(grafo1);

        grafo1.addEdge(parola2, parola1, null);
        grafo1.addEdge(parola2, parola4, null);

        System.out.println("4-------numero nodi ora");
        System.out.println(grafo1.numNodes());
        System.out.println("4-------numero archi ora : ");
        System.out.println(grafo1.numEdges());
        System.out.println("dopo di aggiungere vicini");
        PrintNodeNeighbours(grafo1);

        grafo1.removeNode(parola4);

        System.out.println("Dopo aver rimosso parola");
        PrintNode(grafo1.getNodes());

        System.out.println("5-------numero nodi ora");
        System.out.println(grafo1.numNodes());
        System.out.println("5-------numero archi ora : ");
        System.out.println(grafo1.numEdges());
        PrintNodeNeighbours(grafo1);

        grafo1.removeEdge(parola, parola3);
        grafo1.addEdge(parola, parola2, null);
        grafo1.addEdge(parola, parola1, null);
        grafo1.addEdge(parola1, parola3, null);

        System.out.println("6-------numero nodi ora");
        System.out.println(grafo1.numNodes());
        System.out.println("6-------numero archi ora : ");
        System.out.println(grafo1.numEdges());

        System.out.println("Dopo aver rimosso un arco e aggiunto qualchde arco");
        PrintNodeNeighbours(grafo1);

        grafo1.removeNode(parola);
        grafo1.removeNode(parola1);
        grafo1.removeNode(parola2);
        grafo1.removeNode(parola3);
        grafo1.removeNode(parola4);

        System.out.println("7-------numero nodi ora");
        System.out.println(grafo1.numNodes());
        System.out.println("7-------numero archi ora : ");
        System.out.println(grafo1.numEdges());
        System.out.println("Dopo aver rimosso tutti i nodi:");
        PrintNode(grafo1.getNodes());
        PrintNodeNeighbours(grafo1);

        grafo1.addEdge("Giorgio", "Marco", null);
        grafo1.addEdge("Giorgio", "Giovanni", null);
        grafo1.addEdge("Marco", "Giovanni", null);
        grafo1.addEdge("Michele", "Giorgio", null);

        System.out.println("8-------numero nodi ora");
        System.out.println(grafo1.numNodes());
        System.out.println("8-------numero archi ora : ");
        System.out.println(grafo1.numEdges());

        System.out.println("Dopo aver inserito altri archi con nuovi nodi");
        PrintNode(grafo1.getNodes());
        System.out.println("");
        PrintNodeNeighbours(grafo1);

        System.out.println("CONTROLLO SPAZZATURA");
        for (AbstractEdge<String, Integer> element : grafo1.getEdges()) {
            System.out.println(element.getStart());
            System.out.println(element.getEnd());

        }

        System.out.println("numero archi ora");
        System.out.println(grafo1.numEdges());

        System.out.println("prove contains: Marco, Giovanni, Cacchina, GIanluca,Michele");
        System.out.println(grafo1.containsNode("Marco"));
        System.out.println(grafo1.containsNode("Giovanni"));
        System.out.println(grafo1.containsNode("Cacchina"));
        System.out.println(grafo1.containsNode("Gianmarco"));
        System.out.println(grafo1.containsNode("Michele"));

        System.out.println("prove contains edge: Michele Giorgio, Gianluca Giovanni, Marco michele, Marco Giovanni");
        System.out.println(grafo1.containsEdge("Michele", "Giorgio"));
        System.out.println(grafo1.containsEdge("Gianmarco", "Giovanni"));
        System.out.println(grafo1.containsEdge("Marco", "Michele"));
        System.out.println(grafo1.containsEdge("Marco", "Giovanni"));
    }

}
