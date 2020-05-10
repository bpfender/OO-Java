import Expression.Context;
import Expression.Expression;
import Parser.Parser;

public class Test {
    public static void main(String[] args) {
        System.out.println("TEST Started");

        Parser parser = new Parser();
        Context context = new Context();
        Expression expression;

        expression = parser.parseQuery("create database markbook;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");

            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        expression = parser.parseQuery("use markbook;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("create table marks(name, mark, pass);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("insert into marks values ('Steve', 65, true);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("insert into marks values ('Dave', 55, true);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("insert into marks values ('Bob', 35, false);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("insert into marks values ('Clive', 20, false);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("select * from marks;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("select * from marks where name != 'Dave';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("select * from marks where pass == true;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("update marks set mark = 38 where name =='Clive';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("select * from marks where name == 'Clive';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("delete from marks where name == 'Dave';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("select * from marks;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("delete from marks where mark < 40;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("select * from marks;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("use imdb;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("drop table actors;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("drop table movies;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("drop table roles;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("drop database imdb;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("create database imdb;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("use imdb;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("create table actors (name, nationality, awards);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO actors VALUES ('Hugh Grant', 'British', 3);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO actors VALUES ('Toni Collette', 'Australian', 12);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO actors VALUES ('James Caan', 'American', 8);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO actors VALUES ('Emma Thompson', 'British', 10);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("CREATE TABLE movies (name, genre);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO movies VALUES ('Mickey Blue Eyes', 'Comedy');");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO movies VALUES ('About a Boy', 'Comedy');");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO movies VALUES ('Sense and Sensibility', 'Period Drama');");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT id FROM movies WHERE name == 'Mickey Blue Eyes';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT id FROM movies WHERE name == 'About a Boy';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT id FROM movies WHERE name == 'Sense and Sensibility';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT id FROM actors WHERE name == 'Hugh Grant';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT id FROM actors WHERE name == 'Toni Collette';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT id FROM actors WHERE name == 'James Caan';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT id FROM actors WHERE name == 'Emma Thompson';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("CREATE TABLE roles (name, movie_id, actor_id);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO roles VALUES ('Edward', 3, 1);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO roles VALUES ('Frank', 1, 3);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO roles VALUES ('Fiona', 2, 2);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("INSERT INTO roles VALUES ('Elinor', 3, 4);");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");

        }

        expression = parser.parseQuery("SELECT * FROM actors WHERE awards < 5;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("ALTER TABLE actors ADD age;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT * FROM actors;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("UPDATE actors SET age = 45 WHERE name == 'Hugh Grant';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT * FROM actors WHERE name == 'Hugh Grant';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT nationality FROM actors WHERE name == 'Hugh Grant';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("ALTER TABLE actors DROP age;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("SELECT * FROM actors WHERE name == 'Hugh Grant';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");

        }

        expression = parser.parseQuery("SELECT * FROM actors WHERE (awards > 5) AND (nationality == 'British');");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");

        }

        expression = parser.parseQuery(
                "SELECT * FROM actors WHERE (awards > 5) AND ((nationality == 'British') OR (nationality == 'Australian'));");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");

        }

        expression = parser.parseQuery("SELECT * FROM actors WHERE name LIKE 'an';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");

        }

        expression = parser.parseQuery("SELECT * FROM actors WHERE awards >= 10;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("DELETE FROM actors WHERE name == 'Hugh Grant';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("DELETE FROM actors WHERE name == 'James Caan';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        expression = parser.parseQuery("DELETE FROM actors WHERE name == 'Emma Thompson';");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.exit(1);

        expression = parser.parseQuery("JOIN actors AND roles ON id AND actor_id;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println("PARSE SUCCESS");
            try {
                String result = expression.interpret(context);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }
}