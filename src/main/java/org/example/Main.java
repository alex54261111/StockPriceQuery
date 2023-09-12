package tw.org.iii.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class StockConter extends JFrame {
    private int searchSQLId;
    private String searchSQLco;
    private double searchSQLprice;
    private int getID;

    private JButton search;
    private JTextField input;
    private JTextArea log;
    private int idInt;
    private String coStr;
    private double priceDouble;

    private DoCovnerToIntAndDouble convertNunber;
    private doAPISearch getAPIContent;

    public StockConter() {
        search = new JButton("搜尋");
        input = new JTextField("輸入股票代號");
        log = new JTextArea();

        setLayout(new BorderLayout());
        JPanel top = new JPanel(new BorderLayout());
        add(top, BorderLayout.NORTH);
        top.add(search, BorderLayout.EAST);
        top.add(input, BorderLayout.CENTER);
        JScrollPane jsp = new JScrollPane(log);
        add(jsp, BorderLayout.CENTER);

        input.setFont(new Font(null, Font.BOLD, 23));
        log.setFont(new Font(null, Font.BOLD, 12));

        setTitle("股票合理價試算器");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 250); //setBounds(100,100,500,500);
        setVisible(true);

        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    convertNunber = new DoCovnerToIntAndDouble();
                    convertNunber.setIntRaw(input.getText());
                    getID = convertNunber.DoStrToInt();

                    if (getID >= 9999) {
                        log.append(String.format("輸入錯誤" + "\n"));
                    } else {
                        String oder = "select * from Stock.StockTable where id =" + getID;
                        SQL sql = new SQL(oder);
                        sql.searchSQL();
                        searchSQLId = sql.getId();
                        if (searchSQLId == 0) {
                            log.append(String.format("進入查詢" + "\n"));
                            if (Utils.isFastClick()) {
                                try {
                                    getAPIContent = new doAPISearch(getID);
                                    getAPIContent.GetStockData();
                                    idInt = getAPIContent.getIdInt();
                                    coStr = getAPIContent.getCoStr();
                                    priceDouble = getAPIContent.getPriceDouble();
                                    String SQLEnterOder = "INSERT Stock.StockTable(id,co,price)VALUES(" + idInt + "," + "\'" + coStr + "\'" + "," + priceDouble + ")";
                                    System.out.println(SQLEnterOder);
                                    SQL newSQLData = new SQL(SQLEnterOder);
                                    newSQLData.pushSQL();

                                    log.append(String.format("代號:" + idInt + "公司:" + coStr + "股價:" + priceDouble + "\n"));
                                    if (idInt == 0) {
                                        log.append(String.format("當前股票不存在" + "\n"));
                                        SQLEnterOder = "INSERT Stock.StockTable(id,co,price)VALUES(" + getID + "," + "\'" + coStr + "\'" + "," + priceDouble + ")";
                                        System.out.println(SQLEnterOder);
                                        newSQLData.pushSQL();
                                    }
                                } catch (Exception e3) {
                                    System.out.println("e3:未知錯誤" + e3);
                                    log.append(String.format("當前股票不存在" + "\n"));
                                    String SQLEnterOder = "INSERT Stock.StockTable(id,co,price)VALUES(" + getID + "," + "\'" + coStr + "\'" + "," + priceDouble + ")";
                                    System.out.println(SQLEnterOder);
                                    SQL newSQLData = new SQL(SQLEnterOder);
                                    newSQLData.pushSQL();
                                }
                            } else {
                                log.append(String.format("搜尋間隔過快" + "\n"));
                            }
                        } else  {
                            try {
                                searchSQLco = sql.getCo();
                                searchSQLprice = sql.getPrice();
                                if (searchSQLco == null || searchSQLprice == 0.0) {
                                    log.append(String.format("當前股票不存在或為上櫃公司" + "\n"));
                                } else {
                                    log.append(String.format("代號:" + searchSQLId + "公司:" + searchSQLco + "股價:" + searchSQLprice + "\n"));
                                    //"代號%d:公司%%:股價%f\n", formSQLId, formSQLco, formSQLprice
                                }
                            } catch (Exception e2) {
                                System.out.println("e2" + e2);
                            }
                        }

                    }
                } catch (java.lang.NumberFormatException wrongNunber) {
                    System.out.println(wrongNunber);
                    log.append(String.format("輸入錯誤" + "\n"));
                } catch (Exception e1) {
                    System.out.println("e1" + e1);
                    log.append(String.format("e1" + "\n"));
                }
            }
        });
    }

    public class Utils {
        // 兩次點擊按鈕之間的點擊間隔不能少於2000毫秒
        private static final int MIN_CLICK_DELAY_TIME = 2000;
        private static long lastClickTime;

        public static boolean isFastClick() {
            boolean flag = false;
            long curClickTime = System.currentTimeMillis();
            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                flag = true;
            }
            lastClickTime = curClickTime;
            return flag;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        new StockConter();
    }
}