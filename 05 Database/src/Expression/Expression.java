package Expression;

//https://lukaszwrobel.pl/blog/interpreter-design-pattern/

//https://www.baeldung.com/java-interpreter-pattern
//https://www.oodesign.com/interpreter-pattern.html
//http://ns.inria.fr/ast/sql/index.html
//https://www.red-gate.com/simple-talk/sql/database-administration/sql-server-storage-internals-101/
//https://www.eandbsoftware.org/how-a-database-index-can-help-performance
//https://medium.com/yugabyte/a-busy-developers-guide-to-database-storage-engines-the-basics-6ce0a3841e59
//https://dzone.com/articles/state-storage-engine
//https://stackoverflow.com/questions/1052189/how-to-write-a-simple-database-engine
//https://softwareengineering.stackexchange.com/questions/121653/create-my-own-database-system
//https://www.quora.com/How-does-a-relational-DBMS-internally-store-its-data-In-what-type-of-data-structure-How-does-it-offer-the-rapid-retrieval-without-loading-the-entire-database-into-the-main-memory-I-have-heard-many-DBMS-use-B-trees?share=1
//http://ns.inria.fr/ast/sql/index.html
//https://shardingsphere.apache.org/document/current/en/features/sharding/principle/parse/
//https://stackoverflow.com/questions/10379956/parsing-sql-like-syntax-design-pattern
//https://medium.com/@denisanikin/what-an-in-memory-database-is-and-how-it-persists-data-efficiently-f43868cff4c1
//https://dzone.com/articles/how-three-fundamental-data-structures-impact-stora
//https://www.tutorialcup.com/dbms/selection-algorithm.htm

public interface Expression {
    public String interpret(Context context);

}