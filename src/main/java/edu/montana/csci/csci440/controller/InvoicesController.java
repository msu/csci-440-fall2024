package edu.montana.csci.csci440.controller;

import edu.montana.csci.csci440.model.Invoice;
import edu.montana.csci.csci440.util.Web;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class InvoicesController extends BaseController {
    public static void init(){
        /* READ */
        get("/invoices", (req, resp) -> {
            List<Invoice> invoices = Invoice.all(Web.getCurrentPage(), Web.PAGE_SIZE);
            return renderTemplate("templates/invoices/index.vm",
                    "invoices", invoices);
        });

        get("/invoices/:id", (req, resp) -> {
            Invoice invoice = Invoice.find(asInt(req.params(":id")));
            return renderTemplate("templates/invoices/show.vm",
                    "invoice", invoice);
        });
    }
}
