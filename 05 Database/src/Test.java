import Database.DatabaseHandler;
import Interpreter.Context;
import Interpreter.Expression;
import Parser.Parser;

public class Test {
    public static void main(String[] args) {
        System.out.println("TEST Started");

        Parser parser = new Parser();
        Context context = new Context();
        Expression expression;

        try {

            expression = parser.parseQuery("create database markbook;");
            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {

            expression = parser.parseQuery("use markbook;");
            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {

            expression = parser.parseQuery("create table marks(name, mark, pass);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("insert into marks values ('Steve', 65, true);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("insert into marks values ('Dave', 55, true);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("insert into marks values ('Bob', 35, false);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("insert into marks values ('Clive', 20, false);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("select * from marks;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("select * from marks where name != 'Dave';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("select * from marks where pass == true;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("update marks set mark = 38 where name =='Clive';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("select * from marks where name == 'Clive';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("delete from marks where name == 'Dave';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("select * from marks;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("delete from marks where mark < 40;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("select * from marks;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("use imdb;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("drop table actors;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("drop table movies;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("drop table roles;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("drop database imdb;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("create database imdb;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("use imdb;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("create table actors (name, nationality, awards);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO actors VALUES ('Hugh Grant', 'British', 3);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO actors VALUES ('Toni Collette', 'Australian', 12);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO actors VALUES ('James Caan', 'American', 8);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO actors VALUES ('Emma Thompson', 'British', 10);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("CREATE TABLE movies (name, genre);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO movies VALUES ('Mickey Blue Eyes', 'Comedy');");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO movies VALUES ('About a Boy', 'Comedy');");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO movies VALUES ('Sense and Sensibility', 'Period Drama');");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT id FROM movies WHERE name == 'Mickey Blue Eyes';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT id FROM movies WHERE name == 'About a Boy';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT id FROM movies WHERE name == 'Sense and Sensibility';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT id FROM actors WHERE name == 'Hugh Grant';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT id FROM actors WHERE name == 'Toni Collette';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT id FROM actors WHERE name == 'James Caan';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT id FROM actors WHERE name == 'Emma Thompson';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("CREATE TABLE roles (name, movie_id, actor_id);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO roles VALUES ('Edward', 3, 1);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO roles VALUES ('Frank', 1, 3);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO roles VALUES ('Fiona', 2, 2);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("INSERT INTO roles VALUES ('Elinor', 3, 4);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors WHERE awards < 5;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("ALTER TABLE actors ADD age;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("UPDATE actors SET age = 45 WHERE name == 'Hugh Grant';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors WHERE name == 'Hugh Grant';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT nationality FROM actors WHERE name == 'Hugh Grant';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("ALTER TABLE actors DROP age;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors WHERE name == 'Hugh Grant';");
            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors WHERE (awards > 5) AND (nationality == 'British');");
            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery(
                    "SELECT * FROM actors WHERE (awards > 5) AND ((nationality == 'British') OR (nationality == 'Australian'));");
            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors WHERE name LIKE 'an';");
            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors WHERE awards >= 10;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("DELETE FROM actors WHERE name == 'Hugh Grant';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("DELETE FROM actors WHERE name == 'James Caan';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("DELETE FROM actors WHERE name == 'Emma Thompson';");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("JOIN actors AND roles ON id AND actor_id;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery(" JOIN movies AND roles ON id AND movie_id;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM roles;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM crew;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT spouse FROM actors;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors);");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors WHERE name == 'Hugh Grant;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors WHERE name > 10;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery(" SELECT name age FROM actors;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors awards > 10;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors WHERE name LIKE 10;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("SELECT * FROM actors WHERE awards > 10;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            expression = parser.parseQuery("USE ebay;");

            String result = expression.interpret(context);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}