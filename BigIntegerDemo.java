import java.math.BigInteger;


public class BigIntegerDemo 
{

  public BigInteger one = new BigInteger("1");
  private static final int NUM_BITS = 1024; 

  public BigInteger ModExp( BigInteger M, BigInteger e, BigInteger n, 
                            BigInteger Mbar, BigInteger xbar)
  {
    for (int i=NUM_BITS-1; i>=0; i--)
    {
      // square
      xbar = montMult(xbar,xbar,n);

      // multiply   
      if (e.testBit(i)) // if (e.bit(i) == 1)
        xbar = montMult(Mbar,xbar,n);
    }
    // undo montgomery residue transformation
    return montMult(xbar,one,n);
  }

  
  public BigInteger montMult(BigInteger X, BigInteger Y, BigInteger M)
  {
    BigInteger S = new BigInteger("0"); 
    
    for (int i=0; i<NUM_BITS; i++)
    {
      // add product of X.get_bit(i) and Y to partial sum
      if (X.testBit(i))
        S = S.add(Y); // S += X[i]*Y

      // if S is even, add modulus to partial sum
      if (S.testBit(0))  
        S = S.add(M);     

      // rightshift 1 bit (divide by 2)
      S = S.shiftRight(1);
    }

    // bring back to under 1024 bits by subtracting modulus
    if (S.compareTo(M) >= 0)
      S = S.subtract(M);

    return S;
  }


  public static void main(String[] args) 
  {
    BigIntegerDemo demo = new BigIntegerDemo();

    int testcase = 0;

    String Mstr,estr,nstr,Mbarstr,xbarstr;

    switch (testcase) 
    {
      case 0:
        Mstr = "00be5416af9696937b7234421f7256f78dba8001c80a5fdecdb4ed761f2b7f955946ec920399f23ce9627f66286239d3f20e7a46df185946c6c8482e227b9ce172dd518202381706ed0f91b53c5436f233dec27e8cb46c4478f0398d2c254021a7c21596b30f77e9886e2fd2a081cadd3faf83c86bfdd6e9daad12559f8d2747";
        estr = "6f1e6ab386677cdc86a18f24f42073b328847724fbbd293eee9cdec29ac4dfe953a4256d7e6b9abee426db3b4ddc367a9fcf68ff168a7000d3a7fa8b9d9064ef4f271865045925660fab620fad0aeb58f946e33bdff6968f4c29ac62bd08cf53cb8be2116f2c339465a64fd02517f2bafca72c9f3ca5bbf96b24c1345eb936d1";
        nstr = "b4d92132b03210f62e52129ae31ef25e03c2dd734a7235efd36bad80c28885f3a9ee1ab626c30072bb3fd9906bf89a259ffd9d5fd75f87a30d75178b9579b257b5dca13ca7546866ad9f2db0072d59335fb128b7295412dd5c43df2c4f2d2f9c1d59d2bb444e6dac1d9cef27190a97aae7030c5c004c5aea3cf99afe89b86d6d";
        Mbarstr = "9A9D95D8EE88E38C18FF90DCDDFA8D8B59E8E3457F635660241E4B0CB01AD15CFDB7727BE260BA7254001D0D1B0DF4335927FE9332B9409A3B3D8F6DA56DE4ED030A9DAF7364871E5E46A01E174D36BEF53BB2C823A3301027168A23E67F5ABE4F7E1C3B2D75862C822D1B26593402E8835719CA67428A1F4020F14379EBB84D";
        xbarstr = "4B26DECD4FCDEF09D1ADED651CE10DA1FC3D228CB58DCA102C94527F3D777A0C5611E549D93CFF8D44C0266F940765DA600262A028A0785CF28AE8746A864DA84A235EC358AB97995260D24FF8D2A6CCA04ED748D6ABED22A3BC20D3B0D2D063E2A62D44BBB19253E26310D8E6F5685518FCF3A3FFB3A515C306650176479293";
        break;


      default:
        Mstr = "";
        estr = "";
        nstr = "";
        Mbarstr = "";
        xbarstr = "";
        break;
    }

    // Declare constants 
    BigInteger M = new BigInteger(Mstr,16);
    BigInteger e = new BigInteger(estr,16);
    BigInteger n = new BigInteger(nstr,16);
    BigInteger Mbar = new BigInteger(Mbarstr,16);
    BigInteger xbar = new BigInteger(xbarstr,16); 

    // radix
    BigInteger r = new BigInteger("2");
    r = r.pow(1024);
    
    // compute Mbar 
    //BigInteger Mbar = M.multiply(r);
    //Mbar = Mbar.mod(n);

    // compute xbar 
    //BigInteger xbar = r.mod(n);

    // Compute actual solution 
    BigInteger sol = M.modPow(e,n);
    System.out.println("Actual solution = "+sol);
    System.out.println("Sizes: ");
    System.out.println('\t' + "Mbar = " + Mbar.bitLength() + "bytes");
    System.out.println('\t' + "xbar = " + xbar.bitLength() + "bytes");

    // perform modular exponentiation 
    BigInteger mySol = demo.ModExp(M,e,n,Mbar,xbar);
    System.out.println("Computed Answer: " + mySol);
     
  }
}

