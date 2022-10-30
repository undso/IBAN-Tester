package de.friedrichs.ibantester;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigInteger;
import java.util.stream.IntStream;

@SpringBootApplication
@Log4j2
public class IbanTesterApplication extends JFrame {

    public IbanTesterApplication() {
        initUI();
    }

    public static void main(String[] args) {
        var ctx = new SpringApplicationBuilder(IbanTesterApplication.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {

            var ex = ctx.getBean(IbanTesterApplication.class);
            ex.setVisible(true);
        });
    }

    private void initUI() {

        var quitButton = new JButton("Quit");

        quitButton.addActionListener((ActionEvent event) -> System.exit(0));

        BorderLayout borderLayout = new BorderLayout(10, 10);
        setLayout(borderLayout);

        JTextArea textArea = createTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        setTitle("IBAN Tester");

        var doButton = new JButton("Prüfen");
        doButton.addActionListener((ActionEvent e) -> doCheck(textArea));
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(doButton);
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void doCheck(JTextArea textArea) {
        String ibans = textArea.getText();
		if (ibans.isEmpty()) return;
        String[] split = ibans.split(System.lineSeparator());
        StringBuilder result = new StringBuilder();
		for (String iban: split) {
			result.append(iban.replaceAll(" ", "").toUpperCase())
					.append("\t")
					.append(isValid(iban)?"":"NOK")
					.append(System.lineSeparator());
		}
		textArea.setText(result.toString());
        textArea.grabFocus();
        textArea.selectAll();
    }

    private JTextArea createTextArea() {
        var area = new JTextArea("Hier die IBANs...", 20, 25);
        area.setEnabled(true);
        area.grabFocus();
        area.selectAll();
        return area;
    }

    private boolean isValid(String iban) {
        iban = iban.replaceAll(" ", "").toUpperCase();
        log.info("Teste IBAN {}", iban);
        String[] split = iban.split("");
		IntStream.range(0, split.length).forEach(i -> split[i] = replaceChar(split[i]));
		BigInteger pruef = new BigInteger(split[2] + split[3]);
		log.info("Prüfzimmer ist {}", pruef);
		split[2] = "0";
		split[3] = "0";
		StringBuilder newIban = new StringBuilder();
		for(int i = 4; i<split.length; i++){
			newIban.append(split[i]);
		}
		newIban.append(split[0]).append(split[1]).append(split[2]).append(split[3]);
		BigInteger toCheck = new BigInteger(newIban.toString());
		log.info("Teste: {}", toCheck);
		BigInteger mod = toCheck.mod(BigInteger.valueOf(97));
		BigInteger subtract = BigInteger.valueOf(98).subtract(mod);
		log.info("Result: {}", subtract);
		return subtract.equals(pruef);
    }

    private String replaceChar(String s) {
        switch (s) {
            case "A":
                return "10";
            case "B":
                return "11";
            case "C":
                return "12";
            case "D":
                return "13";
            case "E":
                return "14";
            case "F":
                return "15";
            case "G":
                return "16";
            case "H":
                return "17";
            case "I":
                return "18";
            case "J":
                return "19";
            case "K":
                return "20";
            case "L":
                return "21";
            case "M":
                return "22";
            case "N":
                return "23";
            case "O":
                return "24";
            case "P":
                return "25";
            case "Q":
                return "26";
            case "R":
                return "27";
            case "S":
                return "28";
            case "T":
                return "29";
            case "U":
                return "30";
            case "V":
                return "31";
            case "W":
                return "32";
            case "X":
                return "33";
            case "Y":
                return "34";
            case "Z":
                return "35";
            default:
                return s;
        }
    }
}
