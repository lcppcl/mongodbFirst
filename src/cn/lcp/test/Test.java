package cn.lcp.test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static java.util.Arrays.asList;

public class Test {
    public static void main(String args[]) {
        //创建数据库连接对象
        MongoClient mongoClient = new MongoClient();
        //选择需要操作的数据库，没有就创建
        MongoDatabase db = mongoClient.getDatabase("new_lcp_test");
        //时间格式
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        //往数据库插入一条数据
        db.getCollection("restaurants").insertOne(
                new Document("address", new Document().append("street", "2 Avenue").append("zipcode", "10075").append("building", "1480").append("coord", asList(-73.9557413, 40.7720266)))
                        .append("borough", "Manhattan")
                        .append("cuisine", "Italian")
                        .append("grades", asList(new Document().append("grade", "A").append("score", 11),
                                new Document()
                                        .append("grade", "B")
                                        .append("score", 17)))
                        .append("name", "Vella")
                        .append("restaurant_id", "41704620"));
        //查找集合的所有数据
       /* FindIterable<Document> iterable = db.getCollection("restaurants").find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });*/

        //按条件查询new Document(key,value)
        /*1.形如 new Document( <field>, new Document( <operator>, <value> ) )
        FindIterable<Document> iterable = db.getCollection("restaurants").find(
                new Document("borough", "Manhattan"));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });*/
        //形如lt(<field>, <value>)小于    gt(<field>, <value>)大于
       /* FindIterable<Document> iterable = db.getCollection("restaurants").find(eq("borough", "Manhattan"));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });*/
        //大于的使用
        /*FindIterable<Document> iterable = db.getCollection("restaurants").find(
                new Document("grades.score", new Document("$gt", 30)));  //db.getCollection("restaurants").find(gt("grades.score", 30));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });*/

        //多个条件查询或者db.getCollection("restaurants").find(and(eq("cuisine", "Italian"), eq("address.zipcode", "10075")));
       /* FindIterable<Document> iterable = db.getCollection("restaurants").find(
                new Document("cuisine", "Italian").append("address.zipcode", "10075"));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });*/

        //只要一个条件满足或者db.getCollection("restaurants").find(or(eq("cuisine", "Italian"), eq("address.zipcode", "10075")));
        /*FindIterable<Document> iterable = db.getCollection("restaurants").find(
                new Document("$or", asList(new Document("cuisine", "Italian"),
                        new Document("address.zipcode", "10075"))));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });*/


        //排序-两条件都满足的或者db.getCollection("restaurants").find().sort(ascending("borough", "address.zipcode"));
       /* FindIterable<Document> iterable = db.getCollection("restaurants").find()
                .sort(new Document("borough", 1).append("address.zipcode", 1));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });*/

        //更新数据   第一个是查找条件，第二个是要更新的值，最后在后面添加了一条新的数据
       /* db.getCollection("restaurants").updateOne(new Document("name", "Vella"),
                new Document("$set", new Document("cuisine", "American (New)"))
                        .append("$currentDate", new Document("lastModified", true)));*/

        //更新数据   new Document("address.zipcode", "10075").append("cuisine", "Other"),这两个条件同时满足
      /*  db.getCollection("restaurants").updateMany(new Document("address.zipcode", "10075").append("cuisine", "Other"),
                new Document("$set", new Document("cuisine", "Category To Be Determined"))
                        .append("$currentDate", new Document("lastModified", true)));*/

        //替换文档的所有信息，除去id之外的所有信息
        /*db.getCollection("restaurants").replaceOne(new Document("restaurant_id", "41704620"),
                new Document("address",
                        new Document()
                                .append("street", "2 Avenue")
                                .append("zipcode", "10075")
                                .append("building", "1480")
                                .append("coord", asList(-73.9557413, 40.7720266)))
                        .append("name", "Vella 2"));*/


        //删除数据  删除满足条件的值
        /*db.getCollection("restaurants").deleteMany(new Document("borough", "Manhattan"));*/

        //删除所有的数据
        /*db.getCollection("restaurants").deleteMany(new Document());*/

        //删除数据集合
        /*db.getCollection("restaurants").drop();*/


        //分组求和 以 new Document("_id", "$borough")为条件进行分组求和
       /* AggregateIterable<Document> iterable = db.getCollection("restaurants").aggregate(asList(
                new Document("$group", new Document("_id", "$borough").append("count", new Document("$sum", 1)))));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        });*/


        //
       /* AggregateIterable<Document> iterable = db.getCollection("restaurants").aggregate(asList(
                new Document("$match", new Document("borough", "Queens").append("cuisine", "Brazilian")),
                new Document("$group", new Document("_id", "$address.zipcode").append("count", new Document("$sum", 1)))));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        });*/

        //创建索引  并且升序
        db.getCollection("restaurants").createIndex(new Document("cuisine", 1));
        //创建索引  双从索引，第一个升序，第二个降序
        db.getCollection("restaurants").createIndex(new Document("cuisine", 1).append("address.zipcode", -1));
    }

}
