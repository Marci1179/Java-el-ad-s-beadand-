package com.example.ea_beadando;

import com.oanda.v20.pricing.ClientPrice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import soapclient.MNBArfolyamServiceSoap;
import soapclient.MNBArfolyamServiceSoapImpl;
import com.oanda.v20.Context;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;

@Controller
public class MyController {

    private final Context ctx = new Context(Config.URL, Config.TOKEN);

    public MyController() {

    }

    @GetMapping("/")
    public String semmi(Model model) {
        return "redirect:/fooldal";
    }

    @GetMapping("/fooldal")
    public String fooldal(Model model) {
        return "fooldal";
    }

    @GetMapping("/soap")
    public String showSoapPage(Model model) {
        model.addAttribute("param", new QueryForm());
        return "soap";
    }

    @PostMapping("/soap")
    public String handleSoapRequest(@ModelAttribute("param") QueryForm param, Model model) {
        try {
            MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
            MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();
            String xml = service.getExchangeRates(param.getStartDate(), param.getEndDate(), param.getCurrency());

            List<String> labels = new ArrayList<>();
            List<BigDecimal> values = new ArrayList<>();

            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new java.io.ByteArrayInputStream(xml.getBytes()));
            doc.getDocumentElement().normalize();

            NodeList dayNodes = doc.getElementsByTagName("Day");
            for (int i = 0; i < dayNodes.getLength(); i++) {
                Element day = (Element) dayNodes.item(i);
                String dateStr = day.getAttribute("date");
                NodeList rateNodes = day.getElementsByTagName("Rate");
                for (int j = 0; j < rateNodes.getLength(); j++) {
                    Element rate = (Element) rateNodes.item(j);
                    if (param.getCurrency().equals(rate.getAttribute("curr"))) {
                        String raw = rate.getTextContent().trim().replace(" ", "").replace(",", ".");
                        values.add(new BigDecimal(raw));
                        labels.add(dateStr);
                    }
                }
            }

            java.util.Collections.reverse(labels);
            java.util.Collections.reverse(values);

            model.addAttribute("currency", param.getCurrency());
            model.addAttribute("start", param.getStartDate());
            model.addAttribute("end", param.getEndDate());
            model.addAttribute("labels", labels);
            model.addAttribute("values", values);
            model.addAttribute("count", values.size());
        } catch (Exception e) {
            model.addAttribute("error", "Hiba történt: " + e.getMessage());
        }
        return "soap";
    }

    @GetMapping("/facc")
    public String facc(Model model) {
        try {
            Context ctx = new Context(Config.URL, Config.TOKEN);
            AccountSummary acc = ctx.account.summary(Config.ACCOUNTID).getAccount();
            model.addAttribute("acc", acc);
        } catch (Exception e) {
            model.addAttribute("error", "Nem sikerült lekérni a számlaadatokat: " + e.getMessage());
        }
        return "facc";
    }

    @GetMapping("/faktar")
    public String faktarForm(Model model) {
        model.addAttribute("param", new MessageActPrice());
        model.addAttribute("price", null);
        return "faktar";
    }

    @PostMapping("/faktar")
    public String faktarSubmit(@ModelAttribute MessageActPrice param, Model model) {
        String result = "";
        List<String> instruments = new ArrayList<>();
        instruments.add(param.getInstrument());
        try {
            Context ctx = new Context(Config.URL, Config.TOKEN);
            PricingGetRequest request = new PricingGetRequest(Config.ACCOUNTID, instruments);
            PricingGetResponse resp = ctx.pricing.get(request);
            for (ClientPrice price : resp.getPrices()) {
                result += price + "<br>";
            }
        } catch (Exception e) {
            result = "Hiba történt: " + e.getMessage();
        }
        model.addAttribute("param", param);
        model.addAttribute("price", result);
        return "faktar";  // ugyanazt a faktar.html-t tölti vissza
    }

    @GetMapping("/fhistar")
    public String fhistar(Model model)
    {
        return "fhistar";
    }

    @GetMapping("/fnyit")
    public String fnyit(Model model) {
        return "fnyit";
    }

    @GetMapping("/fpoz")
    public String fpoz(Model model) {
        return "fpoz";
    }

    @GetMapping("/fzar")
    public String fzar(Model model)
    {
        return "fzar";
    }
}
