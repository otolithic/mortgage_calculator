//Kate Flanagan
//CS 2336.501
//Assignment 5: MortgageCalculator
//Made and compiled with jGRASP

/*
   MORTGAGE CALCULATOR PROGRAM
   
   Displays a GUI for a mortgage calculator similar to the one at
   http://www.bankrate.com/calculators/mortgages/mortgage-calculator.aspx
   
   It contains fields for the mortgage amount, duration, and interest rate,
   as well as extra monthly, yearly, or one-time payments.
   
   If the "Show/recalculate amortization table" button is clicked, a new
   frame will appear displaying the amortization table for the mortgage.
   You can exit this frame without exiting the calculator.
   
   If any extra payments were specified, these will show up in the table
   as well as affect the end date of the mortgage, displayed at the bottom.
*/

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.GregorianCalendar;
import java.text.DecimalFormat;

public class MortgageCalculator extends JFrame implements ActionListener
{
   
   //////////////////////MAIN CALCULATION FIELDS///////////////////////
   //change to JFormattedTextFields, this will account for some input validation too
   private JTextField textMortgageAmount = new JTextField(10);
   private JTextField textMortgageTermMonths = new JTextField(10);
   private JTextField textInterestRate = new JTextField(10);
   private JTextField textMonthlyPayment = new JTextField("press calculate!", 20);
   
   static String[] monthList = new String[]{"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};
   
   private JComboBox<String> boxMonth = new JComboBox<>(monthList);
   private JComboBox<String> boxDay;   
   private JComboBox<String> boxYear;
   
   //////////////////////EXTRA PAYMENT FIELDS////////////////////////
   private JTextField textExtraMonthly = new JTextField(10);
   private JTextField textExtraYearly = new JTextField(10);
   private JComboBox<String> boxExtraYearlyMonth = new JComboBox<>(monthList);
   private JTextField textOneTime = new JTextField(10);
   private JComboBox<String> boxOneTimeMonth = new JComboBox<>(monthList);
   private JComboBox<String> boxOneTimeYear; //= new JComboBox<>(new String[]{"1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","2020"});
   
   private JTextField textPaidOffDate = new JTextField("press calculate!", 10);

   
   ////////BUTTONS////////
   private JButton calcButton = new JButton("Calculate");
   private JButton amortization = new JButton("Show/recalculate amortization table");
   
   
   
   
   
   //////////////////////MAIN METHOD/////////////////////
   public static void main(String[] args)
   {
      MortgageCalculator frame = new MortgageCalculator();
      frame.pack();
      frame.setTitle("MortgageCalculator");
      frame.setLocation(new Point(200, 100));
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
      
      
   }
   
   
   
   
   
   //constructor. builds the GUI for the mortgage calculator and
   //contains the method to respond to either button click
   public MortgageCalculator()
   {         
      //instantiate boxDay's list of days with a loop
      String[] dayList = new String[31];
      for(int i = 0; i < dayList.length; i++)
      {
         dayList[i] = Integer.toString(i+1);
      }
      boxDay = new JComboBox<>(dayList);
      
      //instantiate yearList's list of years with a loop
      String[] yearList = new String[30];
      for(int i = 0; i < yearList.length; i++)
      {
         yearList[i] = Integer.toString(i+1990);
      }
      boxYear = new JComboBox<>(yearList);
      
      String[] yearList2 = new String[60];
      for(int i = 0; i < yearList2.length; i++)
      {
         yearList2[i] = Integer.toString(i+1990);
      }
      boxOneTimeYear = new JComboBox<>(yearList2);     
      
      ////////////////////CREATE PANELS/////////////////////
      JPanel dataFields = new JPanel(new GridLayout(5,2));
      
      JPanel holdAmt = new JPanel(new FlowLayout(FlowLayout.LEFT));
      holdAmt.add(new JLabel("$"));
      holdAmt.add(textMortgageAmount);
      
      JPanel holdMonths = new JPanel(new FlowLayout(FlowLayout.LEFT));
      holdMonths.add(textMortgageTermMonths);
      holdMonths.add(new JLabel(" months"));
      
      JPanel holdRate = new JPanel(new FlowLayout(FlowLayout.LEFT));
      holdRate.add(textInterestRate);
      holdRate.add(new JLabel("% per year"));
      
      JPanel holdDate = new JPanel(new FlowLayout());
      holdDate.add(boxMonth);
      holdDate.add(boxDay);
      holdDate.add(boxYear);
      
      JPanel holdPayments = new JPanel(new FlowLayout(FlowLayout.LEFT));
      holdPayments.add(new JLabel("$"));
      holdPayments.add(textMonthlyPayment);
      
      JPanel panelExtraMonthly = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelExtraMonthly.add(new JLabel("Adding: $"));
      panelExtraMonthly.add(textExtraMonthly);
      panelExtraMonthly.add(new JLabel(" to your monthly mortgage payment"));
      
      JPanel panelExtraYearly = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelExtraYearly.add(new JLabel("Adding: $"));
      panelExtraYearly.add(textExtraYearly);
      panelExtraYearly.add(new JLabel(" as an extra yearly mortgage payment every "));
      panelExtraYearly.add(boxExtraYearlyMonth);
      
      JPanel panelOneTime = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelOneTime.add(new JLabel("Adding: $"));
      panelOneTime.add(textOneTime);
      panelOneTime.add(new JLabel(" as a one-time payment in "));
      panelOneTime.add(boxOneTimeMonth);
      panelOneTime.add(boxOneTimeYear);
      
      JPanel panelPaidOff = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panelPaidOff.add(new JLabel("Paid off date will be: "));
      panelPaidOff.add(textPaidOffDate);
      
      JPanel calcButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JPanel extraPayments = new JPanel(new GridLayout(5,2));
      JPanel amortButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      
      ///////////////set main datafield textfields and labels////////////////////
      dataFields.setBorder(new TitledBorder("Enter mortgage amount, term, rate, and start date."));
      dataFields.add(new JLabel("Mortgage amount: $"));
      dataFields.add(holdAmt);
      
      dataFields.add(new JLabel("Mortgage term : "));
      dataFields.add(holdMonths);
      
      dataFields.add(new JLabel("Interest rate: "));
      dataFields.add(holdRate);
      
      dataFields.add(new JLabel("Mortgage start date: "));
      dataFields.add(holdDate);
      
      dataFields.add(new JLabel("Your monthly payments will be: "));
      dataFields.add(holdPayments);
      
      //add calcButton
      calcButtonPanel.add(calcButton);
      
      /////////////set extra payment textfields and labels//////////////     
      extraPayments.setBorder(new TitledBorder("Extra Payments"));
      extraPayments.add(panelExtraMonthly);
      extraPayments.add(panelExtraYearly);
      extraPayments.add(panelOneTime);
      extraPayments.add(panelPaidOff);
      extraPayments.add(amortButtonPanel);
      
      //add amortization button
      amortButtonPanel.add(amortization);
      
      //arrange panels properly
      add(dataFields, BorderLayout.NORTH);
      add(calcButtonPanel, BorderLayout.CENTER);
      add(extraPayments, BorderLayout.SOUTH);    
      
      //add ActionListeners to buttons
      calcButton.addActionListener(this);
      amortization.addActionListener(this);
   }
   
   
   
   
   
   
   //////////////////////////////////ACTION LISTENER/////////////////////////////////////
   public void actionPerformed(ActionEvent e)
   {
      double amount;
      int months;
      double rate;
      GregorianCalendar date = new GregorianCalendar();
      int startYear;
      int startMonth;
      int startDay;
      GregorianCalendar endDate = new GregorianCalendar();
      String endYear;
      String endMonth;
      String endDay;
      
      double extraMonthly;
      double extraYearly;
      int extraYearlyMonth;
      double extraOneTime;
      int extraOneTimeMonth;
      int extraOneTimeYear;
      
      DecimalFormat trunc = new DecimalFormat("0.00");    
      
      try
      {
         amount = Double.parseDouble(textMortgageAmount.getText());
         months = Integer.parseInt(textMortgageTermMonths.getText());
         rate = Double.parseDouble(textInterestRate.getText());
         if (amount <= 0 || months <= 0 || rate <= 0)
            throw new java.lang.NumberFormatException("Please enter positive values");
         
         startYear = Integer.parseInt(boxYear.getSelectedItem().toString());
         startMonth = boxMonth.getSelectedIndex();
         startDay = Integer.parseInt(boxDay.getSelectedItem().toString());
         
         //throw an exception (NumberFormatException because we have a catch statement for that)
         //if the user has entered a nonexistent date like February 30 or June 31
         if (startMonth == 1 && startDay > 28 && !(startDay == 29 && startYear%4 == 0))
            throw new java.lang.NumberFormatException("That date doesn't exist");
         if ((startMonth == 3 || startMonth == 5 || startMonth == 8 || startMonth == 8 || startMonth == 10) && startDay == 31)
            throw new java.lang.NumberFormatException("That date doesn't exist");
         
         //set date to the selected values
         date.set(startYear, startMonth, startDay);
                  
         
         //create a  mortgage object
         Mortgage mortgage = new Mortgage(amount, rate, months, date);
         
         //set "extra" values
         extraMonthly = Double.parseDouble(textExtraMonthly.getText());
         extraYearly = Double.parseDouble(textExtraYearly.getText());     
         extraOneTime = Double.parseDouble(textOneTime.getText());
         
         if (extraMonthly < 0 || extraYearly < 0 || extraOneTime < 0)
            throw new java.lang.NumberFormatException("No negative numbers please");
         
         extraYearlyMonth = boxExtraYearlyMonth.getSelectedIndex();
         extraOneTimeMonth = boxOneTimeMonth.getSelectedIndex();
         extraOneTimeYear = Integer.parseInt(boxOneTimeYear.getSelectedItem().toString());
         
         mortgage.addMonthlyPayments(extraMonthly);
         mortgage.addYearlyPayments(extraYearly, extraYearlyMonth);
         mortgage.addOneTimePayment(extraOneTime, extraOneTimeMonth, extraOneTimeYear);
         
         //calculate a monthly payment and put it in the proper text field
         textMonthlyPayment.setText(trunc.format(mortgage.getMonthlyPayment()));
          
         //calculate when the mortgage will be paid off
         endDate = mortgage.getPaidOffDate();      
         textPaidOffDate.setText(getDateAsString(endDate));
         
         //if the amortization button was clicked...
         if (e.getSource() == amortization)
         {
            //create an AmortizationTable object. its constuctor will build and display the table
            AmortizationTable table = new AmortizationTable(mortgage);
            table.setTitle("Amortization table for " + amount + " borrowed on " + getDateAsString(mortgage.getStartDate()));
            table.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            table.setSize(600,480);
            table.setLocation(new Point(this.getX() + 510, this.getY()));
            table.setVisible(true);
            
            
            
            
            
            //set the new paid-off date (will change if extra payments included in calculation)
            textPaidOffDate.setText(getDateAsString(table.getNewEndDate()));
         }
      }
      catch(java.lang.NumberFormatException ex)
      {
         //display an error message if any fields are empty or not positive numbers
         JOptionPane.showMessageDialog(null, ex, "error", JOptionPane.ERROR_MESSAGE);
      }
   }
   
   
   
   //returns a given date as a string
   public static String getDateAsString(GregorianCalendar d)
   {
      String dateAsString = "";
      
      dateAsString += MortgageCalculator.monthList[d.get(GregorianCalendar.MONTH)] + " ";
      dateAsString += Integer.toString(d.get(GregorianCalendar.DAY_OF_MONTH)) + ", ";
      dateAsString += Integer.toString(d.get(GregorianCalendar.YEAR));
      
      return dateAsString;
   }

}

/*
                     MORTGAGE CLASS
   Mortgage objects contain attributes corresponding to the
   mortgage amount, interest rate, length of mortgage, and
   start date of the mortgage.
   
   This class contains 2 constructors, 1 default and one with
   4 arguments corresponding to the aforementioned attributes.
   
   It contains a method to calculate the monthly payment on a
   mortgage, and another method to calculate the end date of a
   mortgage.
*/
class Mortgage
{
   private double mortgageAmount;
   private double interestRate;
   private int numberOfMonths;
   private GregorianCalendar startDate;
   
   private double monthlyExtra;
   private double yearlyExtra;
   private int yearlyExtraMonth;
   private double onceExtra;
   private int onceExtraMonth;
   private int onceExtraYear;
   
   //default constructor for mortgage class;
   public Mortgage()
   {
       this(165000,7.0,360,new GregorianCalendar());
   }
   
   //constructor, accepts mortgage amount, interest rate, number of months of the mortgage, and payment start date
   public Mortgage(double mortgageAmount, double interestRate, int numberOfMonths, GregorianCalendar startDate)
   {
      this.mortgageAmount = mortgageAmount;
      this.interestRate = interestRate;
      this.numberOfMonths = numberOfMonths;
      this.startDate = startDate;
   }
   
   //set an extra monthly payment value
   public void addMonthlyPayments(double extraM)
   {
      monthlyExtra = extraM;
   }
   
   //set an extra yearly payment value and the month for it to be added
   public void addYearlyPayments(double extraY, int monthY)
   {
      yearlyExtra = extraY;
      yearlyExtraMonth = monthY;
   }
   
   //set a one-time extra payment value and the month and year for it to be added
   public void addOneTimePayment(double extraO, int monthO, int yearO)
   {
      onceExtra = extraO;
      onceExtraMonth = monthO;
      onceExtraYear = yearO;
   }
   
   //returns the mortgage amount
   public double getMortgageAmount()
   {
      return mortgageAmount;
   }
   
   //returns the number of months the mortgage will last
   public int getMortgageLength()
   {
      return numberOfMonths;
   }
   
   //return the interest rate for a mortgage
   public double getInterestRate()
   {
      return interestRate;
   }
   
   //return the start date of a mortgage
   public GregorianCalendar getStartDate()
   {
      return startDate;
   }
   
   //calculates the monthly payment for a mortgage object
   public double getMonthlyPayment()
   {
      double monthlyPayment = mortgageAmount * (interestRate/1200) / (1-(1/Math.pow(1 + (interestRate/1200), numberOfMonths)));
      
      return monthlyPayment;
   }
   
   //return the extra monthly payment for this mortgage
   public double getExtraMonthlyPayment()
   {
      return monthlyExtra;
   }
   
   //return the extra yearly payment for this mortgage
   public double getExtraYearlyPayment()
   {
      return yearlyExtra;
   }
   
   //return the one-time extra payment for this mortgage
   public double getExtraOneTimePayment()
   {
      return onceExtra;
   }
   
   //return an int indicating the month in which an extra yearly payment will be applied
   public int getExtraYearlyPaymentMonth()
   {
      return yearlyExtraMonth;
   }
   
   //return an int indicating the month in which a one-time extra payment will be applied
   public int getExtraOneTimePaymentMonth()
   {
      return onceExtraMonth;
   }
   
   //return an int indicating the year in which a one-time extra payment will be applied
   public int getExtraOneTimePaymentYear()
   {
      return onceExtraYear;
   }
   
   //calculates the date when the mortgage will be paid off
   public GregorianCalendar getPaidOffDate()
   {
      GregorianCalendar paidOffDate = new GregorianCalendar();
      
      //create a clone of the starting date and add the
      //number of months of the mortgage to it
      paidOffDate = (GregorianCalendar)startDate.clone();
      paidOffDate.add(GregorianCalendar.MONTH, numberOfMonths);
      
      return paidOffDate;  
   }
}

/*
               AMORTIZATION TABLE CLASS
               
   Has one constructor which accepts an instance of the
   mortgage class and creates a JTextArea within a JScrollPane
   to display the amortization table for this mortgage.
   
   It also contains a method to calculate a new end date for the
   mortgage based on the last payment in the table. This is
   only important when extra payments are involved which change
   the duration of the mortgage.
*/
class AmortizationTable extends JFrame 
{
   private JScrollPane holdTable; 
   private JTextArea paymentInfo;
   private double currentBalance;
   private GregorianCalendar currentDate;
   private double interestCost;
   private double principal;
   private double payment;
   private double monthlyInterest;
   private double totalInterest;
   
   //create a DecimalFormat for dollar amounts
   private DecimalFormat dollars = new DecimalFormat("$0.00");
   
   //only constructor for AmortizationTable. accepts a Mortgage object as an argument
   //then creates a JScrollPane containing a JTextArea to display mortgage info
   public AmortizationTable(Mortgage m) 
   {  
      paymentInfo = new JTextArea();
      paymentInfo.setEditable(false);
      holdTable = new JScrollPane(paymentInfo);
      add(holdTable);
      
      monthlyInterest = m.getInterestRate()/1200;
      currentBalance = m.getMortgageAmount();
      currentDate = (GregorianCalendar)m.getStartDate().clone();
      payment = m.getMonthlyPayment() + m.getExtraMonthlyPayment();
      
      paymentInfo.setText("Month / Year\tPayment\tPrincipal Paid\tInterest Paid\tTotal Interest\tBalance \n");
      
      //while the balance has not yet reached zero,
      //increment/decrement the current date and payment values as needed
      //then display them in the table
      while (currentBalance-payment > 0)
      {
         currentDate.add(GregorianCalendar.MONTH, 1);
         
         //recalculate payment value if there are extra payments
         if(currentDate.get(GregorianCalendar.MONTH) == m.getExtraYearlyPaymentMonth())
            payment += m.getExtraYearlyPayment();
            
         if(currentDate.get(GregorianCalendar.MONTH) == m.getExtraOneTimePaymentMonth()
            && currentDate.get(GregorianCalendar.YEAR) == m.getExtraOneTimePaymentYear())
            payment += m.getExtraOneTimePayment(); 
         
         //calculate other values
         interestCost = monthlyInterest * currentBalance;
         principal = payment - interestCost;
         currentBalance -= principal;  
         totalInterest += interestCost;
         
         //append info to the table
         paymentInfo.append(MortgageCalculator.getDateAsString(currentDate) + "\t");
         paymentInfo.append(dollars.format(payment) + "\t");
         paymentInfo.append(dollars.format(principal) + "\t");
         paymentInfo.append(dollars.format(interestCost) + "\t");
         paymentInfo.append(dollars.format(totalInterest) + "\t");
         paymentInfo.append(dollars.format(currentBalance) + "\n");
         
         //undo the changes made to the payment value when the extra payment has been made
         if(currentDate.get(GregorianCalendar.MONTH) == m.getExtraYearlyPaymentMonth())
            payment -= m.getExtraYearlyPayment();
         
         if(currentDate.get(GregorianCalendar.MONTH) == m.getExtraOneTimePaymentMonth()
            && currentDate.get(GregorianCalendar.YEAR) == m.getExtraOneTimePaymentYear())
            payment -= m.getExtraOneTimePayment();               
      }
      
      //if the remaining balance is less than a payment amount, different calculations are required
      if(currentBalance-payment <= 0)
      {
         interestCost = monthlyInterest * currentBalance;
         principal = currentBalance;
         payment = principal + interestCost;
         currentBalance = 0;
         currentDate.add(GregorianCalendar.MONTH, 1);
         
         paymentInfo.append(MortgageCalculator.getDateAsString(currentDate) + "\t");
         paymentInfo.append(dollars.format(payment) + "\t");
         paymentInfo.append(dollars.format(principal) + "\t");
         paymentInfo.append(dollars.format(interestCost) + "\t");
         paymentInfo.append(dollars.format(totalInterest) + "\t");
         paymentInfo.append(dollars.format(currentBalance) + "\n");
      }
      
   }
   
   //return the NEW mortgage end date,
   //as determined by currentDate's value at the end of the table
   public GregorianCalendar getNewEndDate()
   {      
      return currentDate;
   }
}