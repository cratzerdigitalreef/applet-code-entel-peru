package com.imox.carrierprivileges;

import uicc.usim.toolkit.USATEnvelopeHandler;
import uicc.access.*;
//import sim.access.*;

import javacard.framework.ISO7816;

import javacard.framework.AID;
import javacard.framework.APDU;
import javacard.framework.ISOException;
import javacard.framework.JCSystem;
import javacard.framework.Shareable;
import javacard.framework.Util;

//BECAUSE OF RELEASE 5
/*
import sim.toolkit.EnvelopeHandler;
import sim.toolkit.MEProfile;
import sim.toolkit.ProactiveHandler;
import sim.toolkit.ProactiveResponseHandler;
import sim.toolkit.ToolkitConstants;
import sim.toolkit.ToolkitException;
import sim.toolkit.ToolkitInterface;
import sim.toolkit.ToolkitRegistry;
*/
import uicc.toolkit.EnvelopeHandler;
import uicc.toolkit.EnvelopeHandlerSystem;
import uicc.toolkit.ProactiveHandler;
import uicc.toolkit.TerminalProfile;
import uicc.toolkit.ToolkitConstants;
import uicc.usim.access.USIMConstants;
import uicc.toolkit.ProactiveHandlerSystem;
import uicc.toolkit.ProactiveResponseHandler;
import uicc.toolkit.ProactiveResponseHandlerSystem;
import uicc.toolkit.ToolkitException;
import uicc.toolkit.ToolkitInterface;
import uicc.toolkit.ToolkitRegistry;
import uicc.toolkit.ToolkitRegistrySystem;
import uicc.usim.toolkit.USATEnvelopeHandler;
import uicc.usim.toolkit.USATEnvelopeHandlerSystem;

import javacard.framework.TransactionException;

public class carrierprivileges extends javacard.framework.Applet 
implements ToolkitInterface, uicc.toolkit.ToolkitConstants {
// FOR RELEASE 5	implements ToolkitInterface, ToolkitConstants {
	
	
	//COTA APPLET VERSION - TO BE USED WHEN THE CONSOLE IS READY
	/* */
    static final byte[] COTA = { 
    		(byte)'C', (byte)'O', (byte)'T', (byte)'A', 
    		(byte)'0', (byte)'2', (byte)'0', (byte)'9'
    		, (byte)'D'
    		};
    /* */

	private static ToolkitRegistry mReg;
	//private SIMView mFv;
	//private byte[] byteArray1 = JCSystem.makeTransientByteArray((short) 255,JCSystem.CLEAR_ON_RESET);
	static final byte PROFILE_LAUNCH_BROWSER = (byte) 70;
	
	private static short sTPDALength;
	private static byte baTPDA_Address[];
	
	//FOR SMSPP
    static final short MAXLEN = (short)240;

	//final static private short IOD_TPDU_LENGTH               = (short) 205;// 205;
	final static private short IOD_TPDU_LENGTH               = (short) MAXLEN;
	private byte[] byteArray2 = JCSystem.makeTransientByteArray((short) 70,JCSystem.CLEAR_ON_RESET);	
	//private byte[] byteArray2 = new byte[(short)IOD_TPDU_LENGTH];
	
	static byte[] MCC = new byte[(short)3];
	static byte[] MNC = new byte[(short)3];

	// ****************************************************************************************************
	// Welcome Message - ARRAY LENGTH (bytes): 240 - DATA LENGTH (characters): 111 (0x6F) - PADDING LENGTH (bytes): 129
	// VALUE: Bienvenido a Claro. Vive la mejor experiencia en nuestra red y conoce todos los beneficios que tenemos para ti.

	static byte[] mainMsg = new byte[] { (byte)0x6F,
		(byte) 'B',(byte) 'i',(byte) 'e',(byte) 'n',(byte) 'v',(byte) 'e',(byte) 'n',(byte) 'i',
		(byte) 'd',(byte) 'o',(byte) ' ',(byte) 'a',(byte) ' ',(byte) 'C',(byte) 'l',(byte) 'a',
		(byte) 'r',(byte) 'o',(byte) '.',(byte) ' ',(byte) 'V',(byte) 'i',(byte) 'v',(byte) 'e',
		(byte) ' ',(byte) 'l',(byte) 'a',(byte) ' ',(byte) 'm',(byte) 'e',(byte) 'j',(byte) 'o',
		(byte) 'r',(byte) ' ',(byte) 'e',(byte) 'x',(byte) 'p',(byte) 'e',(byte) 'r',(byte) 'i',
		(byte) 'e',(byte) 'n',(byte) 'c',(byte) 'i',(byte) 'a',(byte) ' ',(byte) 'e',(byte) 'n',
		(byte) ' ',(byte) 'n',(byte) 'u',(byte) 'e',(byte) 's',(byte) 't',(byte) 'r',(byte) 'a',
		(byte) ' ',(byte) 'r',(byte) 'e',(byte) 'd',(byte) ' ',(byte) 'y',(byte) ' ',(byte) 'c',
		(byte) 'o',(byte) 'n',(byte) 'o',(byte) 'c',(byte) 'e',(byte) ' ',(byte) 't',(byte) 'o',
		(byte) 'd',(byte) 'o',(byte) 's',(byte) ' ',(byte) 'l',(byte) 'o',(byte) 's',(byte) ' ',
		(byte) 'b',(byte) 'e',(byte) 'n',(byte) 'e',(byte) 'f',(byte) 'i',(byte) 'c',(byte) 'i',
		(byte) 'o',(byte) 's',(byte) ' ',(byte) 'q',(byte) 'u',(byte) 'e',(byte) ' ',(byte) 't',
		(byte) 'e',(byte) 'n',(byte) 'e',(byte) 'm',(byte) 'o',(byte) 's',(byte) ' ',(byte) 'p',
		(byte) 'a',(byte) 'r',(byte) 'a',(byte) ' ',(byte) 't',(byte) 'i',(byte) '.',
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF
		};
		// ****************************************************************************************************
		
		
		// ****************************************************************************************************
		// SHA-1 - ARRAY LENGTH (bytes): 20 - DATA LENGTH (bytes): 20 (0x14)
		// VALUE: F4653BEE3D83F5EFE18960B53594438EB1E3B299
		
		static byte[] APP_HASH = new byte[] { 
		(byte)0xF4,(byte)0x65,(byte)0x3B,(byte)0xEE,
		(byte)0x3D,(byte)0x83,(byte)0xF5,(byte)0xEF,
		(byte)0xE1,(byte)0x89,(byte)0x60,(byte)0xB5,
		(byte)0x35,(byte)0x94,(byte)0x43,(byte)0x8E,
		(byte)0xB1,(byte)0xE3,(byte)0xB2,(byte)0x99
		};
		// ****************************************************************************************************
		
		
		// ****************************************************************************************************
		// URL for LAUNCH BROWSER - ARRAY LENGTH (bytes): 100 - DATA LENGTH (characters): 82 (0x52) - PADDING LENGTH (bytes): 18
		// VALUE: https://cota-sdk.mnc020.mcc334.pub.3gppnetwork.org:8181/applet/119?mcc=111&mnc=111
		
		static byte[] URL = new byte[] { (byte)0x52,
		(byte) 'h',(byte) 't',(byte) 't',(byte) 'p',(byte) 's',(byte) ':',(byte) '/',(byte) '/',
		(byte) 'c',(byte) 'o',(byte) 't',(byte) 'a',(byte) '-',(byte) 's',(byte) 'd',(byte) 'k',
		(byte) '.',(byte) 'm',(byte) 'n',(byte) 'c',(byte) '0',(byte) '2',(byte) '0',(byte) '.',
		(byte) 'm',(byte) 'c',(byte) 'c',(byte) '3',(byte) '3',(byte) '4',(byte) '.',(byte) 'p',
		(byte) 'u',(byte) 'b',(byte) '.',(byte) '3',(byte) 'g',(byte) 'p',(byte) 'p',(byte) 'n',
		(byte) 'e',(byte) 't',(byte) 'w',(byte) 'o',(byte) 'r',(byte) 'k',(byte) '.',(byte) 'o',
		(byte) 'r',(byte) 'g',(byte) ':',(byte) '8',(byte) '1',(byte) '8',(byte) '1',(byte) '/',
		(byte) 'a',(byte) 'p',(byte) 'p',(byte) 'l',(byte) 'e',(byte) 't',(byte) '/',(byte) '1',
		(byte) '1',(byte) '9',(byte) '?',(byte) 'm',(byte) 'c',(byte) 'c',(byte) '=',(byte) '1',
		(byte) '1',(byte) '1',(byte) '&',(byte) 'm',(byte) 'n',(byte) 'c',(byte) '=',(byte) '1',
		(byte) '1',(byte) '1',
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
		(byte)0xFF,(byte)0xFF
		};
	// ****************************************************************************************************
	
    /* Variables applet imox */
    static final byte[] HEADER = { (byte) 0xFF, (byte) 0x40, (byte) 0x1A, (byte) 0xe2, (byte) 0x18, (byte) 0xE1,
        (byte) 0x16, (byte) 0xC1, (byte) 0x14 };

    
 // ****************************************************************************************************

	static boolean bFirstStatusCommand = true;
	//DEBUGGING
	//static short nFirstStatusCommand = (short)0;

    static byte TIMER_ID = (byte) 0xFF;
	//static final short LENGTH_APP_HASH = (short) 20;
	// TIEMPO EN SEGUNDOS, HEXADECIMAL
	/*
	 * Coding:
- byte 3: hour; this byte is coded exactly in the same way as the hour field of the
TP-Service-Centre-Time-Stamp in TS 123 040 [27];
- byte 4: minute; this byte is coded exactly in the same way as the minute field of the
TP-Service-Centre-Time-Stamp in TS 123 040 [27];
- byte 5: second; this byte is coded exactly in the same way as the second field of the
TP-Service-Centre-Time-Stamp in TS 123 040 [27].
    Example:
    23:01:56 Mar 25th 2013 PST (GMT-7) would be encoded as 31 30 52 32 10 65 8A.
    
    0x32 => 23 hs
    0x95 => 59 min
    0x95 => 59 sec
	 */
    //static final byte[] TIMER_VALUE_INIT = { (byte) 0x32, (byte) 0x95, (byte) 0x95 };
    static byte[] TIMER_VALUE_INIT = { (byte) 0x32, (byte) 0x95, (byte) 0x95 };
    static final byte[] TIMER_VALUE_NULL = { (byte) 0x00, (byte) 0x00, (byte) 0x00 };
	
	//byte[] temporal;
	//private final byte[] persistence;
	//FOR RECEIVING A MESSAGE FROM OTA TO INFORM TO THE APK - ANDROID
	private byte bFAValue = (byte)0x00;
	
	static final byte CLA_APP 			= (byte) 0x80;
	static final byte INS_GET_DATA 		= (byte) 0xCA;
    static final byte INS_GET_RESPONSE 	= (byte) 0x02;
    static final byte[] RESPUESTA = { (byte)'O', (byte)'K' };
    
    static final byte APK_APDU_NOTRECEIVED 	= (byte) 0x00;
    static final byte APK_APDU_RECEIVED 	= (byte) 0xF1;

    static byte bAPK_APDU_VAL = APK_APDU_NOTRECEIVED;
    static short nAPK_APDU_MAX_Counter = (short)0;
    static short nAPK_APDU_MAX_Counter_Backup = (short)0;
    static short nAPK_APDU_Counter_Backup = (short)0;
    
    /*************************/

    //***********************************************************************
    //ADDED 2021-08-02
    //***********************************************************************
    //***********************************************************************
	//EVENT_FORMATTED_SMS_PP_ENV - Static variable in interface uicc.usim.toolkit.ToolkitConstants
	//Event : Envelope SMS-PP Data Download (31.115 formatted) = 2
    final static private short EVENT_FORMATTED_SMS_PP_ENV = (short)2;
	//EVENT_FORMATTED_SMS_PP_UPD - Static variable in interface uicc.usim.toolkit.ToolkitConstants
	//Event : Update Record EF sms APDU (31.115 formatted) = 3
    final static private short EVENT_FORMATTED_SMS_PP_UPD = (short)3;
    //EVENT_UNFORMATTED_SMS_PP_ENV - Static variable in interface uicc.usim.toolkit.ToolkitConstants
    //Event : Envelope SMS-PP Data Download unformatted sms = 4
    final static private short EVENT_UNFORMATTED_SMS_PP_ENV = (short)4;
    //EVENT_UNFORMATTED_SMS_PP_UPD - Static variable in interface uicc.usim.toolkit.ToolkitConstants
    //Event : Update Record EFsms APDU unformatted sms = 5
    final static private short EVENT_UNFORMATTED_SMS_PP_UPD = (short)5;
    
	/** Offset of the length of the OTA command inside the buffer */
	private final static short INCOMING_SMS_CHL_OFFSET 										= (short)0x06;

	/**
	 * SMS general Info
	 */

    /* RAM
    private byte[] generalBuffer = JCSystem.makeTransientByteArray(IOD_BUFFER_LENGTH, JCSystem.CLEAR_ON_RESET);
    */
    //EEPROM
    static private byte[] generalBuffer                      = new byte [(short) IOD_TPDU_LENGTH ];
	private short lengthMsg = (short)0;
	
	//FOR SAVING IMEI + LAUNCH BROWSER SUPPORTED
	private static final short nIMEILaunchBrowserSupportedIndex = (short)8;
	private byte[] IMEIOld = new byte [(short)(nIMEILaunchBrowserSupportedIndex+1)];
	private static boolean bTimerActivateIMEIChange = false;
	
	//FOR DEBUGGING SMSPP
    //static private byte[] generalBufferDebug                 = new byte [(short) 160];
	/*
	 * BYTES NEEDED IN RAM:
	 * BYTE  1: N_WAITFORSENDSMS_OFFSET
	 * BYTES 2: N_SMSIMEIRETRIESCNTR_OFFSET
	 * BYTE  1: N_NORESPONSEUSER_OFFSET
	 * BYTES 9: IMEI + LAUNCH BROWSER SUPPORTED
	 * BYTE  1: DISPLAY_RES_OFFSET
	 * BYTE  1: DISPLAY_RES2_OFFSET - standard result
	 * BYTE  1: N_STATUSCOMMAND_FIRST
	 * BYTES 2: N_STATUSCOMMAND_APK_APDU
	 * BYTES 2: N_STATUSCOMMAND_PLOCI
	 * BYTES 2: N_STATUSCOMMAND_PLOCI_APK
	 */
	//private byte[] transByteArray = JCSystem.makeTransientByteArray((short) 18,JCSystem.CLEAR_ON_RESET);	
	private byte[] transByteArray = JCSystem.makeTransientByteArray((short) 22,JCSystem.CLEAR_ON_RESET);	
	private static final short N_WAITFORSENDSMS_OFFSET = (short)0;
	//2 BYTES FOR RETRIES COUNTER
	private static final short N_SMSIMEIRETRIESCNTR_OFFSET 	= (short)1;
	private static final short N_NORESPONSEUSER_OFFSET 		= (short)3;
	//8 BYTES FOR IMEI + LAUNCH BROWSER SUPPORTED
	private static final short TEMP_IMEI_OFFSET 			= (short)4;
	private static final short DISPLAY_RES_OFFSET 			= (short)13;
	private static final short DISPLAY_RES2_OFFSET 			= (short)14;
	private static final short N_STATUSCOMMAND_FIRST 		= (short)15;
	//2 bytes FOR APK_APDU COUNTER
	private static final short N_STATUSCOMMAND_APK_APDU 	= (short)16;
	//2 bytes FOR LOCALIZATION COUNTER
	private static final short N_STATUSCOMMAND_PLOCI 		= (short)18;
	//2 bytes FOR LOCALIZATION COUNTER
	private static final short N_STATUSCOMMAND_PLOCI_APK    = (short)20;

	//9 bytes FOR LOCALIZATION 
	private byte[] loci = JCSystem.makeTransientByteArray((short) 9,JCSystem.CLEAR_ON_RESET);	
	private static short MAX_COUNTER_GETLOCI 		= (byte)0;
	private static short MAX_COUNTER_GETLOCI_APK 	= (byte)0;
	
	//private static byte nWaitForSendSMS = (byte)0x00;
	private static boolean bSendSMSIMEI = false;
	private static final byte MAX_WAIT_SEND_SMS = (byte)3;
	private static final byte MAX_WAIT_STATUSCOMMAND_FIRST = (byte)3;
	
	private static final byte DCS_8_BIT_DATA 	= (byte)0x04;
	private static final byte DCS_UCS2_BIT_DATA = (byte)0x08;
	private static byte DisplayText_DCS_BIT_DATA = DCS_8_BIT_DATA;
	//private static byte DisplayText_DCS_BIT_DATA = DCS_UCS2_BIT_DATA;
	
	private static final byte b31 = 0x31;
	private static final byte b30 = 0x30;
	private static final byte bNULL = (byte)0x00;
	//private static byte[] b0 =  { (byte)0x30, (byte)0x00};
	
	//SMPP COMMANDS
	private static final byte bF0 = (byte)0xF0;
	private static final byte bF1 = (byte)0xF1;
	private static final byte bF2 = (byte)0xF2;
	private static final byte bF4 = (byte)0xF4;
	private static final byte bF5 = (byte)0xF5;
	private static final byte bF8 = (byte)0xF8;
	private static final byte bF9 = (byte)0xF9;
	private static final byte bFA = (byte)0xFA;
	private static final byte bFB = (byte)0xFB;
	private static final byte bFC = (byte)0xFC;
	private static final byte bFD = (byte)0xFD;
	private static final byte bFE = (byte)0xFE;
	private static final byte bE0 = (byte)0xE0;
	private static final byte bE1 = (byte)0xE1;
	
	//RETRIES FOR SMS WITH IMEI
	private static final short nSMSIMEIRetries_MAX = (short)3;
	private static       short nSMSIMEIRetries     = (short)0;
	private static       boolean bSMSIMEIRetries   = false;
	
	//USED WHEN SWITCHED OFF AND ON TERMINAL AND TIMER WAS LOST
	private static boolean bTIMERTerminalProfile   = false;
	
	//30 SECONDS EACH STATUS COMMAND
	//2 STATUS COMMANDS IS ONE MINUTE
	//24 HOURS - 1.440 MINUTES => x 2 => 2.880 STATUS COMMANDS => 0x0B 40
	//private static final short nSMSIMEIRetriesSTCounter_MAX = (short)0x0B40;
	//FOR TESTING WAIT 5 STATUS COMMANDS FOR RETRIES
	private static short nSMSIMEIRetriesSTCounter_MAX = (short)0x0005;
	//private static short nSMSIMEIRetriesSTCounter     = (short)0;
		
	private static final byte RES_CMD_NO_RESPONSE_USER = (byte)0x12;	
	private static final short nNoResponseUserMax = (short)3;
	private static boolean bNoResponseUserRetries = false;
	
	//SUPPORT F2 FOR SMSPP - UPDATE URL
	private static boolean bSMSPP_F2_Supported = false;

	//VALIDATE SMPP TPDA SOURCE
	private static boolean bSMSPP_TPDA_Validate = true;

	
    //***********************************************************************
    //***********************************************************************

	public static carrierprivileges AppletPluginHldr = null;
	 
	public carrierprivileges(byte bArray[], short bOffset, short bLength)
	{	
		register(bArray, (short) (bOffset + 1), bArray[bOffset]);
		//bOffset += (short) (bArray[bOffset] + 1);		
		// skip info
		//bOffset += (short) (bArray[bOffset] + 1);
		//bLength = bArray[bOffset++]; // application spec parameters
		
		//FOR SAVING IN LOG FILE TELECOM\SMS
   		usimFile = UICCSystem.getTheUICCView(JCSystem.CLEAR_ON_RESET);
   		//usimFile = SIMSystem.getTheSIMView();
   		//fWriteLogGeneral(bArray, (short)bOffset, (short)bLength);
   		
		// Inicializacion de variables de control
		//persistence = new byte[(short)1];
		//temporal = JCSystem.makeTransientByteArray((short) 1, JCSystem.CLEAR_ON_RESET);
		//temporal[(short)0] = APK_APDU_NOTRECEIVED;
		//storeVariable();

		/////////////////////////////
		
		//GET TPDA ADDRESS
       	baTPDA_Address = new byte [15];
		
		byte tmp = bArray[bOffset]; // aid length
		bOffset = (short) (bOffset+tmp+1);
		tmp = bArray[bOffset]; // info length
		bOffset = (short) (bOffset+tmp+1);  

		//THEY ARE FOR CHECKING WHTHER THERE ALL THE PARAMETERS EXPECTED
		short nParamsInstallForInstallLen = (short) bArray[bOffset];
		short nParamPos = (short)0;
		
		short i = (short)(bOffset+1);
		sTPDALength = (short)((byte)((byte)(bArray[i] + (byte)1) / (byte)2) + (short)2);
		
		if((short)bArray.length > sTPDALength)
		{
			//05 81 06 01 F4
			Util.arrayCopyNonAtomic(bArray,i,baTPDA_Address,(short)0,sTPDALength);
		}
		i+=(short)sTPDALength;
		bOffset=i;
		nParamPos+=(short)sTPDALength;
		
		//GET TIMER
		if((short)bArray.length > (short)(bOffset+(short)TIMER_VALUE_INIT.length)
				&& nParamPos < nParamsInstallForInstallLen)
		{
			fTimePrepare(bArray, bOffset, TIMER_VALUE_INIT);
		}
		bOffset+=(short)TIMER_VALUE_INIT.length;
		nParamPos+=(short)TIMER_VALUE_INIT.length;
				
		//GET STATUS COMMAND FOR SMS IMEI RETRIES
		if((short)bArray.length > (short)(bOffset+1)
				&& nParamPos < nParamsInstallForInstallLen)
			nSMSIMEIRetriesSTCounter_MAX = Util.makeShort(bArray[bOffset], bArray[(short)(bOffset+1)]);
		bOffset+=(short)2;
		nParamPos+=(short)2;
		
		//GET MCC
		if((short)bArray.length > (short)(bOffset+(short)MCC.length)
				&& nParamPos < nParamsInstallForInstallLen)
		{
			Util.arrayCopyNonAtomic(bArray,bOffset,MCC,(short)0,(short)MCC.length);
			fURLReplaceWithMCC(URL, MCC);
		}
		bOffset+=(short)MCC.length;
		nParamPos+=(short)MCC.length;
		
		//GET MNC
		if((short)bArray.length > (short)(bOffset+(short)MNC.length)
			&& nParamPos < nParamsInstallForInstallLen)
		{
			Util.arrayCopyNonAtomic(bArray,bOffset,MNC,(short)0,(short)MNC.length);
			fURLReplaceWithMNC(URL, MNC);
		}
		bOffset+=(short)MNC.length;
		nParamPos+=(short)MNC.length;
		
		//TIMER ACTIVATED FROM THE TERMINAL PROFILE, NOT WAITTING FOR OTA MESSAGE
		if((short)bArray.length > (short)(bOffset)
				&& nParamPos < nParamsInstallForInstallLen)
		{
			if(bArray[bOffset]==(byte)b31)
				bTIMERTerminalProfile=true;
			else
				bSendSMSIMEI=true;
		}
		bOffset++;
		nParamPos++;
		
		//DISPLAY TEXT NO RESPONSE USER RETRIES
		if((short)bArray.length > (short)(bOffset)
			&& nParamPos < nParamsInstallForInstallLen)
		{
			if(bArray[bOffset]==(byte)b31)
				bNoResponseUserRetries=true;
		}
		bOffset++;
		nParamPos++;

		//F2 Supported for Update URL
		if((short)bArray.length > (short)(bOffset)
			&& nParamPos < nParamsInstallForInstallLen)
		{
			if(bArray[bOffset]==(byte)0x01)
				bSMSPP_F2_Supported=true;
		}
		bOffset++;
		nParamPos++;

		//STATUS COMMANDS FOR COUNTING NOT APK-APDU RECEIVED
		if((short)bArray.length > (short)(bOffset+(short)1)
				&& nParamPos < nParamsInstallForInstallLen)
		{
			nAPK_APDU_MAX_Counter = Util.makeShort(bArray[bOffset], bArray[(short)(bOffset+1)]);
			nAPK_APDU_MAX_Counter_Backup = (short)(nAPK_APDU_MAX_Counter/(short)2);
		}
		bOffset+=(short)2;
		nParamPos+=(short)2;

		//MAX_COUNTER_GETLOCI
		if((short)bArray.length > (short)(bOffset+(short)1)
				&& nParamPos < nParamsInstallForInstallLen)
		{
			MAX_COUNTER_GETLOCI = Util.makeShort(bArray[bOffset], bArray[(short)(bOffset+1)]);
		}
		bOffset+=(short)2;
		nParamPos+=(short)2;
		
		//MAX_COUNTER_GETLOCI_APK
		if((short)bArray.length > (short)(bOffset+(short)1)
				&& nParamPos < nParamsInstallForInstallLen)
		{
			MAX_COUNTER_GETLOCI_APK = Util.makeShort(bArray[bOffset], bArray[(short)(bOffset+1)]);
		}
		bOffset+=(short)2;
		nParamPos+=(short)2;

		//Validate SMPP TPDA SOURCE OR NOT
		if((short)bArray.length > (short)(bOffset)
			&& nParamPos < nParamsInstallForInstallLen)
		{
			if(bArray[bOffset]==bE1)
				bSMSPP_TPDA_Validate=false;
		}
		bOffset++;
		nParamPos++;
		
		//Activate TIMER when IMEI has changed, new mobile
		if((short)bArray.length > (short)(bOffset)
			&& nParamPos < nParamsInstallForInstallLen)
		{
			if(bArray[bOffset]==b31)
				bTimerActivateIMEIChange=true;
		}
		bOffset++;
		nParamPos++;
		
		/* */
		
		// register to the SIM Toolkit Framework
		mReg = ToolkitRegistrySystem.getEntry();
		//mReg = ToolkitRegistry.getEntry();
		
		// evento para la inicializacion del primer timer
        mReg.setEvent((byte)EVENT_PROFILE_DOWNLOAD);
		
        
        // solicitar un timer
        TIMER_ID = mReg.allocateTimer();
        
		// register to the SMS Point-to-Point Data Download Envelope Event
		mReg.setEvent((byte)EVENT_FORMATTED_SMS_PP_ENV);
		mReg.setEvent((byte)EVENT_FORMATTED_SMS_PP_UPD);
		mReg.setEvent((byte)EVENT_UNFORMATTED_SMS_PP_ENV);
		mReg.setEvent((byte)EVENT_UNFORMATTED_SMS_PP_UPD);
   		
		//POLLINTERVAL FOR STATUS COMMAND 30 seconds
		//FOR SENDING SMS
		mReg.requestPollInterval((short)30);
		
	}

	public static void install(byte bArray[], short bOffset, byte bLength) throws ISOException
	{		
		new carrierprivileges(bArray, bOffset, bLength);		
	}

	/*
	 * NOT USED ANYMORE. It was used in previous COTA versions (2.6)
	private void storeVariable() {
		try {
			JCSystem.beginTransaction();
			Util.arrayCopy(temporal,  (short)0, persistence, (short) 0, (short) temporal.length);
			JCSystem.commitTransaction();
		} catch(TransactionException e) {
		}
	}
	*/
	

	private boolean fAPK_APDU_NotReceived()
	{
		if (bAPK_APDU_VAL == (byte) APK_APDU_NOTRECEIVED) 
			return true;
		else
			return false;
	}
	
	private boolean fAPK_APDU_Received()
	{
		return !fAPK_APDU_Received();
	}
	
    /* metodos applet imox */
    public boolean select() {
		if (!mReg.isEventSet((byte)EVENT_TIMER_EXPIRATION)) {
			TIMER_ID = mReg.allocateTimer();
			
			if(fAPK_APDU_NotReceived())
			//if ((byte)persistence[(short)0] == (byte) 0x00) {
				loadTimer();
			//}
		}

		return super.select();
    }
    
    
    private boolean loadTimer() {
    	return loadTimer(TIMER_VALUE_INIT);
    }
    
    private boolean loadTimer(byte[] timerValue) {

    	//COMPARE TIMER TO NULL VALUE
    	if(fTimerNull(timerValue, (short)0))
    		return false;

    	try {

    		if (!mReg.isEventSet((byte)EVENT_TIMER_EXPIRATION)) 
				TIMER_ID = mReg.allocateTimer();
	
			bAPK_APDU_VAL = APK_APDU_NOTRECEIVED;

	    	//if(TIMER_ID == 0x01){
				ProactiveHandler proHdlr = ProactiveHandlerSystem.getTheHandler();
				//ProactiveHandler proHdlr = ProactiveHandler.getTheHandler();

				proHdlr.init(PRO_CMD_TIMER_MANAGEMENT, (byte) 0x00, (byte) DEV_ID_UICC);
				//proHdlr.init((byte)PRO_CMD_TIMER_MANAGEMENT, (byte) 0x00, (byte) DEV_ID_ME);

				proHdlr.appendTLV(TAG_TIMER_IDENTIFIER, TIMER_ID);

		    	proHdlr.appendTLV(TAG_TIMER_VALUE, timerValue, (short) 0x00, (short) timerValue.length);

		    	proHdlr.send();
			//}
	
				//SET TIMER ACTIVATED JUST IN CASE THE TERMINAL IS SWITCHED OFF
				bTIMERTerminalProfile = true;

				return true;
    	}
    	catch(ToolkitException e)
    	{
    		
	    	bFirstStatusCommand=true;
			bTIMERTerminalProfile = true;

    		//HANDLER NOR AVAILABLE = 0x00 02
    		byte bRes = (byte)e.getReason();
	    	//DEBUGGING
	    	fWriteLogShort((short)19, (short)bRes);
	    	
	    	//DEBUGGING
	    	fWriteLogShort((short)20, (short)transByteArray[N_STATUSCOMMAND_FIRST]);
	    	
			/*
			if(transByteArray[N_TIMER_RETRIES_OFFSET] < nNoResponseUserMax)
			{
	    		bFirstStatusCommand=true;
				bTIMERTerminalProfile = true;
			}
			*/

    		return false;
    	}
	}
    
    /********************************/

	public Shareable getShareableInterfaceObject(AID clientAID, byte parameter)
	{
		return (Shareable)this;
	}

	public void processToolkit(short event) throws ToolkitException 
	//public void processToolkit(byte event) throws ToolkitException 
	{
		short nLociCounter=(short)0;
		boolean bLociGet=false;
		boolean bLociGetAndSend=false;
		
		switch (event) 
		{
		case EVENT_TIMER_EXPIRATION:
	
			//SET TIMER TERMINAL PROFILE VARIABLE OFF WHEN TIMER IS EXPIRED
			bTIMERTerminalProfile = false;
			
			if(fAPK_APDU_NotReceived())
			{
	            mReg.releaseTimer(TIMER_ID);
	            
	            //It is set to 0xF1 so that the PopUp is not showed agan.
	            //2022-04-08
	            bAPK_APDU_VAL = APK_APDU_RECEIVED;
	            
				showMessage(true, true, mainMsg, bNULL, bNULL);									
			}
	        
	        break;
	
		case EVENT_PROFILE_DOWNLOAD:
			
			//NOT USED PER NOW BECAUSE IT IS EXPECTED SMSPP FOR ACTIVATION
			/* ONLY FOR TESTING DISPLAY TEXT RETRIES WHEN NO USER RESPONSE (0x12)
			if ((byte)persistence[(short)0] == (byte) 0x00) {
				loadTimer(TIMER_VALUE_INIT);
			}
			*/
			bFirstStatusCommand=true;
			/*
			//CHECK TIMER ACTIVATED JUST IN CASE THE TERMINAL IS SWITCHED OFF
			if(bTIMERTerminalProfile)
			{
				//ADDED 2021-10-07 because of Telcel request.
				bSendSMSIMEI=false;
				loadTimer();
			}
			*/
			
			transByteArray[N_WAITFORSENDSMS_OFFSET] = (byte)0x00;
			transByteArray[N_STATUSCOMMAND_FIRST]	= (byte)0x00;

			//RETRIEVE BACKUP FOR APK APDU COUNTER
			if(nAPK_APDU_MAX_Counter_Backup > 0)
			{
				fromShortToBytesIntoByteArray(nAPK_APDU_Counter_Backup, transByteArray, N_STATUSCOMMAND_APK_APDU);
			}

			break;
	        
		case EVENT_FORMATTED_SMS_PP_ENV:
	    case EVENT_FORMATTED_SMS_PP_UPD:
	    case EVENT_UNFORMATTED_SMS_PP_ENV:
	    case EVENT_UNFORMATTED_SMS_PP_UPD:
			processIncomingMessage();
			break;
			
	    case EVENT_STATUS_COMMAND:
	    	
	    	//DEBUGGING SMSPP
	    	//serviceDisplayText(generalBufferDebug, (short)0, (short)generalBufferDebug.length);
	    	//generalBuffer[(short)0]=bFAValue;
	    	//serviceDisplayText(generalBuffer, (short)0, (short)1);
	    	
	    	if(bFirstStatusCommand)
	    	{
		    	transByteArray[N_STATUSCOMMAND_FIRST]++;
		    	if(transByteArray[N_STATUSCOMMAND_FIRST]>(byte)MAX_WAIT_STATUSCOMMAND_FIRST)
		    	{
		    		bFirstStatusCommand=false;
		
		    		//GET IMEI
					fGetLocalInformationIMEI(transByteArray, TEMP_IMEI_OFFSET, nIMEILaunchBrowserSupportedIndex);
		
					//CHECK TIMER ACTIVATED JUST IN CASE THE TERMINAL IS SWITCHED OFF
					if(bTIMERTerminalProfile)
					{
						//ADDED 2021-12-28 BECAUSE IMEI HAS CHANGED
						if(Util.arrayCompare(transByteArray, TEMP_IMEI_OFFSET, IMEIOld, (short)0, (short)IMEIOld.length)!=(byte)0)
							Util.arrayCopy(transByteArray, TEMP_IMEI_OFFSET, IMEIOld, (short)0, (short)IMEIOld.length);
						
						//ADDED 2021-10-07 because of Telcel request.
						bSendSMSIMEI=false;
						loadTimer();
		
					}
					else
					{
						//ADDED 2021-12-28 BECAUSE IMEI HAS CHANGED
						if(Util.arrayCompare(transByteArray, TEMP_IMEI_OFFSET, IMEIOld, (short)0, (short)IMEIOld.length)!=(byte)0)
						{
							Util.arrayCopy(transByteArray, TEMP_IMEI_OFFSET, IMEIOld, (short)0, (short)IMEIOld.length);
							bSendSMSIMEI=true;
							
							//ADDED FOR ENTEL CHLE - 2022-05-23
							if(bTimerActivateIMEIChange)
								loadTimer();
						}
					}
					
					//DEBUGGING
					//fWriteLog((short)10, IMEIOld, (short)0, (short)IMEIOld.length);

		    	}
	    	}
	    	
			//SEND SMS WITH IMEI FOR CHECKING WHETHER THE TERMINAL SUPPORTS LAUNCH BROWSER
			if(bSendSMSIMEI)
			{
				//nWaitForSendSMS++;
				transByteArray[N_WAITFORSENDSMS_OFFSET]++;
				
				if(transByteArray[N_WAITFORSENDSMS_OFFSET] > MAX_WAIT_SEND_SMS)
				{
					//UPDATED 2021-12-28 SO THAT WHEN IMEI HAS CHANGED IT IS SENT AGAIN THE NEW IMEI
					bSendSMSIMEI=false;
				
					//IT IS ALWAYS SENT THE IMEI, NO MATTER THE MOBILE SUPPORTS LAUNCH BROWSER OR NOT
					//if(fLaunchBrowserCheck())
					//{
						if(!fnsendSms(IMEIOld, (short)0, (short)IMEIOld.length))
						{
							//RETRIES ONLY WHEN SEND SMS WAS NOT OK
							bSMSIMEIRetries = true;
							nSMSIMEIRetries = (short)0;

							//transByteArray[N_SMSIMEIRETRIESCNTR_OFFSET] = (byte)0x00;
							fRAMArrayClean(transByteArray, N_SMSIMEIRETRIESCNTR_OFFSET);
						}
	
						bAPK_APDU_VAL = APK_APDU_NOTRECEIVED;
	
					//}
				}
			
			}

			//RETRIES FOR SENDING SMS
			//nSMSIMEIRetriesSTCounter_MAX should be bigger than 0x00 - Added 2021-10-07
			if(bSMSIMEIRetries && nSMSIMEIRetriesSTCounter_MAX > (short)0)
			{
				//nSMSIMEIRetriesSTCounter++;
				//transByteArray[N_SMSIMEIRETRIESCNTR_OFFSET]++;
				short nSMSIMEIRetriesSTCounter = fRAMArraySumUpShort(transByteArray, N_SMSIMEIRETRIESCNTR_OFFSET);
				
				//if(transByteArray[N_SMSIMEIRETRIESCNTR_OFFSET] > nSMSIMEIRetriesSTCounter_MAX)
				if(nSMSIMEIRetriesSTCounter > nSMSIMEIRetriesSTCounter_MAX)
				{
					
					//transByteArray[N_SMSIMEIRETRIESCNTR_OFFSET] = (byte)0x00;
					fRAMArrayClean(transByteArray, N_SMSIMEIRETRIESCNTR_OFFSET);
					
					nSMSIMEIRetries++;
					
					if(nSMSIMEIRetries < nSMSIMEIRetries_MAX)
					{
						if(fnsendSms(IMEIOld, (short)0, (short)IMEIOld.length))
							//ONLY RETRY WHEN THERE WAS AN ERROR
							bSMSIMEIRetries = false;
					}
					else
						bSMSIMEIRetries = false;
				}
			}
			
			//CHECK WHETHER APK HAS SENT APDU TO APPLET
			if(nAPK_APDU_MAX_Counter > (short)0 && !bFirstStatusCommand)
			{
				short nAPKAPDUCounter = fRAMArraySumUpShort(transByteArray, N_STATUSCOMMAND_APK_APDU);
				if(nAPKAPDUCounter > nAPK_APDU_MAX_Counter)
				{
					//INFORM SERVER WITH DATA BECAUSE THERE IS NO APDU FROM APK
			    	fSendSMSAPKApduStatus(true);

					fRAMArrayClean(transByteArray, N_STATUSCOMMAND_APK_APDU);
				}
				
				//FOR BACKUP COUNTER TAKING INTO ACCOUNT SWITCH OFF AND ON MOBILE
				if(nAPKAPDUCounter > nAPK_APDU_MAX_Counter_Backup)
				{
					nAPK_APDU_Counter_Backup = Util.makeShort(transByteArray[N_STATUSCOMMAND_APK_APDU], transByteArray[(short)(N_STATUSCOMMAND_APK_APDU+1)]);
				}
			}
			
			//CHECK WHETHER GET LOCI
			if(!bFirstStatusCommand && (MAX_COUNTER_GETLOCI > (short)0 || MAX_COUNTER_GETLOCI_APK > (short)0))
			{
				bLociGet=false;
				bLociGetAndSend=false;

				if(MAX_COUNTER_GETLOCI > (short)0)
				{
					//GET LOCI AND SEND TO SERVER
					nLociCounter = fRAMArraySumUpShort(transByteArray, N_STATUSCOMMAND_PLOCI);
					if(nLociCounter > MAX_COUNTER_GETLOCI)
					{
						fRAMArrayClean(transByteArray, N_STATUSCOMMAND_PLOCI);
						bLociGet=true;
						bLociGetAndSend=true;
					}
				}
				if(MAX_COUNTER_GETLOCI_APK > (short)0)
				{
					//GET LOCI FOR APK
					nLociCounter = fRAMArraySumUpShort(transByteArray, N_STATUSCOMMAND_PLOCI_APK);
					if(nLociCounter > MAX_COUNTER_GETLOCI_APK)
					{
						fRAMArrayClean(transByteArray, N_STATUSCOMMAND_PLOCI_APK);
						bLociGet=true;
					}
				}
				
				if(bLociGet)
				{
					//GET LOCI
					fGetLocalInformationLOCI(loci, (short)0);

					if(bLociGetAndSend)
						//INFORM SERVER LOCI
						fSendSMSLoci(true, true);
				}
			}

			break;
		}
	}
	
	private void processIncomingMessage()
	{		
		//short len =(short)0;
		boolean bAddCOTAVer = true;
		
		//EnvelopeHandler  envHdlr;
    	USATEnvelopeHandler  envHdlr;
    	
		short offsetSMS = (short)0;
		short lengthSMS = (short)0;
		short actOffset = (short)0;

		// Copy the user data in tempBuffer[]
		//envHdlr = EnvelopeHandler.getTheHandler();
		envHdlr = USATEnvelopeHandlerSystem.getTheHandler();
		
		//GET TPDA SOURCE
		short nSMSLen = envHdlr.getShortMessageLength();
		//short nSMSLen = envHdlr.getTPUDLOffset();
		envHdlr.copyValue((short)0, generalBuffer, (short)0, nSMSLen);
		
		//FOR DEBUGGING
		fWriteLogAfterSMSPP_TPDA(generalBuffer, (short)0, nSMSLen);
		
		//Example: 0x44 0C 81 21 43 65 87 09 21 7F F6 00 00 00 00 00 00 00 21 02 70
		//TPDA: 0x0C 81 21 43 65 87 09 21
		//VALIDATE THAT ORIGINATING ADDRESS TPDA IS THE SAME AS THE ONE SAVED IN THE APPLET
		if(!fValidateTPDASourceToDestination(generalBuffer, (short)0, (short)nSMSLen, baTPDA_Address, (short)0))
		{
			//TPDA ORIGINATNG ADDRESS IS NOT THE SAME AS THE ONE INSTALLED IN THE APPLET 'baTPDA_Address'
			if(bSMSPP_TPDA_Validate)
			{
				fWriteLog((short)4, baTPDA_Address, (short)0, sTPDALength);
				return;
			}
		}


		//GET DATA FROM SMS
		offsetSMS = envHdlr.getTPUDLOffset();
		lengthSMS = (short) (envHdlr.getValueLength()- offsetSMS -1);
		actOffset = (short)(INCOMING_SMS_CHL_OFFSET +  envHdlr.getValueByte((short)(offsetSMS+INCOMING_SMS_CHL_OFFSET)) + (short)1);
		lengthMsg = (short)((++lengthSMS)-actOffset);

		if(lengthMsg > (short)generalBuffer.length)
			lengthMsg = (short)generalBuffer.length;
		
		envHdlr.copyValue((short)(offsetSMS + actOffset), generalBuffer, (short)0, lengthMsg);

		//FOR DEBUGGING
		fWriteLogAfterSMSPP_Data(generalBuffer, (short)0, lengthMsg);

	    //***********************************************************************************
	
		//RETRIES FOR SHOWING MESSAGE
		transByteArray[N_NORESPONSEUSER_OFFSET] = (byte)0x00;

		short offset=(short)0;
		byte bTagValue = generalBuffer[offset];

	    switch(bTagValue)
	    {
		    case (byte)bF0:
				//transByteArray[N_NORESPONSEUSER_OFFSET] = (byte)0x00;
		    	showMessage(true, true, mainMsg, bF0, bNULL);						
		    	break;
		    	
		    case (byte)bF1:
		    	//UPDATE WELCOME MESSAGE
		    	
		    	if(generalBuffer[(short)(offset+1)]==(byte)bF0)
		    	{
		    		//COMMAND: F1 + F0 + TEXT
		    		//UPDATE WELCOME MESSAGE AND CALL POPUP
	    			offset = fOTADisplayTextCheckDCS(generalBuffer, (short)(offset+2));
		    		fOTASMPPBufferToArrayMsgOrURL(mainMsg, generalBuffer, (short)offset);

					//transByteArray[N_NORESPONSEUSER_OFFSET] = (byte)0x00;
			    	showMessage(true, true, mainMsg, bF1, bF0);
		    	}
	    		else
	    		{
	    			offset = fOTADisplayTextCheckDCS(generalBuffer, (short)(offset+1));
		    		fOTASMPPBufferToArrayMsgOrURL(mainMsg, generalBuffer, offset);
	    		}
		    	break;

		    	/* */
		    case (byte)bF2:
		    	//UPDATE URL FOR LAUNCH BROWSER
		    	
		    	if(bSMSPP_F2_Supported)
		    	{
			    	if(generalBuffer[(short)(offset+1)]==(byte)bF0)
			    	{
			    		//COMMAND: F2 + F0 + URL
			    		//UPDATE URL AND CALL POPUP
			    		fOTASMPPBufferToArrayMsgOrURL(URL, generalBuffer, (short)(offset+1));
			    		
				    	showMessage(true, true, mainMsg, bF2, bF0);
			    	}
			    	else
			    		fOTASMPPBufferToArrayMsgOrURL(URL, generalBuffer, offset);
		    	}
		    	
		    	break;

		    case (byte)bF4:
		    	//LAUNCH BROWSER
		    	
		    	if(bSMSPP_F2_Supported)
		    	{
			    	if(lengthMsg>(short)1)
			    		//THERE IS ALSO THE NEW URL TO CALL
			    		fOTASMPPBufferToArrayMsgOrURL(URL, generalBuffer, offset);
			    		
					showMessage(false, true, mainMsg, bF4, bNULL);									
		    	}
		    	break;
		    	
		    case (byte)bF5:
		    	
		    	//ONLY SHOW POPUP - NOT CALLING URL
    			offset = fOTADisplayTextCheckDCS(generalBuffer, (short)(offset+1));
	    		fOTASMPPBufferToArrayMsgOrURL(mainMsg, generalBuffer, offset);
	    		
		    	showMessage(true, false, mainMsg, bF5, bNULL);
		    	
	    		break;

		    case (byte)bF8:
		    case (byte)bFB:
		    	
		    	//SENDS IMEI
		    	short n=(short)0;
		    	fGetLocalInformationIMEI(IMEIOld, (short)0, nIMEILaunchBrowserSupportedIndex);
		    	Util.arrayCopy(IMEIOld, (short)0, generalBuffer, (short)n, (short)IMEIOld.length);
				n+=(short)IMEIOld.length;
				
				bAddCOTAVer=false;
				if(bTagValue == (byte)bFB)
				{
					bAddCOTAVer=true;
				}

				fSendSMSWithCOTAVer(bAddCOTAVer, generalBuffer, (short)0, (short)n);
		    	break;
		    
		    case (byte)bF9:

		    	bSMSIMEIRetries = false;
		    	nSMSIMEIRetries=(short)0;
		    	//transByteArray[N_SMSIMEIRETRIESCNTR_OFFSET] = (byte)0x00;
				transByteArray[N_SMSIMEIRETRIESCNTR_OFFSET] = (byte)0x00;
				
		    	//ACTIVATE TIMER IF IT IS RECEIVED 0xF9 31
		    	offset++;
		    	if(generalBuffer[(short)(offset)]==(byte)b31)
					loadTimer();
		    	else
		    	{
		    		if((short)(offset+3)<=lengthMsg)
		    		{
				    	//ACTIVATE TIMER IF IT IS RECEIVED 0xF9 xx xx xx [TIMER]
		    			if(fTimePrepare(generalBuffer, offset, TIMER_VALUE_INIT))
		    					loadTimer();
		    		}
		    	}

		    	break;

		    case (byte)bFA:
		    	offset++;
		    	bFAValue  =generalBuffer[(short)offset];
		    	break;

		    case (byte)bFC:
		    	
		    	offset++;
		    	if(lengthMsg>=(short)3)
		    	{
		    		//NEW APK APDU COUNTER FOR SENDING STATUS WHEN THERE IS NO MESSAGE FROM APK TO APPLET
					nAPK_APDU_MAX_Counter = Util.makeShort(generalBuffer[offset], generalBuffer[(short)(offset+1)]);
		    	}
		    	else
		    	{
			    	if(lengthMsg==(short)2)
			    	{
			    		//NEW APK APDU VALUE
			    		bAPK_APDU_VAL = generalBuffer[offset];
			    	}
		    	}
		    	//REPORT TO THE CONSOLE
		    	fSendSMSAPKApduStatus(true);
		    	
		    	break;
		    	
		    case (byte)bFD:
		    	offset++;
		    	if(lengthMsg >= (short) APP_HASH.length)
		    	{
		    		Util.arrayCopy(generalBuffer, offset, APP_HASH, (short)0, (short) APP_HASH.length);
		    	}
		    	
				fSendSMSWithCOTAVer(false, APP_HASH, (short)0, (short)APP_HASH.length);
		    	
		    	break;
		    	
		    case (byte)bFE:
		    case (byte)bE0:
		    	offset++;
		    	if(lengthMsg>=(short)3)
		    	{
		    		if(bTagValue==bFE)
		    			//NEW COUNTER FOR SENDING LOCI TO THE SERVER
		    			MAX_COUNTER_GETLOCI = Util.makeShort(generalBuffer[offset], generalBuffer[(short)(offset+1)]);
		    		if(bTagValue==bE0)
			    		//NEW COUNTER FOR GETTING LOCI FOR APK
			    		MAX_COUNTER_GETLOCI_APK = Util.makeShort(generalBuffer[offset], generalBuffer[(short)(offset+1)]);
		    	}
		    	
		    	//SEND LOCI
		    	fGetLocalInformationLOCI(loci, (short)0);
		    	fSendSMSLoci(true, true);
		    	break;
		    	
		    case (byte)bE1:
		    	//TPDA SMPP VALIDATION
		    	offset++;
		    	if(lengthMsg>(short)1)
		    	{
		    		//ON FOR SMPP TPDA VALIDATION
		    		bSMSPP_TPDA_Validate=true;
		    		//COPY NEW TPDA FOR SMPP VALIDATION
		    		sTPDALength = (short)generalBuffer[offset];
		    		Util.arrayCopyNonAtomic(generalBuffer, (short)(offset+1),baTPDA_Address,(short)0,sTPDALength);
		    		generalBuffer[(short)0]=b31;
				}
		    	else
		    	{
		    		//OFF FOR SMPP TPDA VALIDATION
		    		bSMSPP_TPDA_Validate=false;
		    		generalBuffer[(short)0]=b30;
		    	}
		    	
	    		generalBuffer[(short)1]=(byte)sTPDALength;
		    	//SEND [VALIDATION 0x31 / NOT VALIDATION 0x30] + TPDA NUMBER + COTA VERSION
		    	Util.arrayCopyNonAtomic(baTPDA_Address, (short)0, generalBuffer,(short)2, sTPDALength);
		    	fSendSMSWithCOTAVer(true, generalBuffer, (short)0, (short)(sTPDALength+2));
		    	
		    	break;
		    	
		    default:
		    	break;
	    }       


	}

	private short fOTADisplayTextCheckDCS(byte[] bBuffer, short nStart)
	{
		if(bBuffer[nStart]==DCS_8_BIT_DATA 
				||bBuffer[nStart]==DCS_UCS2_BIT_DATA)
		{
			//private static final byte DCS_8_BIT_DATA 	= (byte)0x04;
			//private static final byte DCS_UCS2_BIT_DATA = (byte)0x08;
			DisplayText_DCS_BIT_DATA=bBuffer[nStart];
			return nStart;
		}
		else
			return (short)(nStart-1);
	}
	
	private void fOTASMPPBufferToArrayMsgOrURL(byte[] ArrayMsgOrURL, byte[] bBuffer, short offset)
	{

		//offset is the command such as F1, F2, F4, etc.
		//then there is the length
		//EXAMPLE:
		//F1 [LEN] Text
		//F2 [LEN] URL
		
    	short len = Util.makeShort((byte)0x00, (byte)bBuffer[(short)(offset+(short)1)]);
    	
		if(len > (short) 0 && len <= (short) (ArrayMsgOrURL.length-(short)1))
		{
			
			Util.arrayFillNonAtomic(ArrayMsgOrURL, (short)0, (short)ArrayMsgOrURL.length, (byte)0xFF);
			
			if(len > (short) 127)
			{
				//THIS IS BECAUSE IT IS RECEIVED LEN + 81
				len = Util.makeShort((byte)0x00, bBuffer[(short)(offset+2)]);
				
				if((short)(len + 1)>(short)ArrayMsgOrURL.length)
					Util.arrayCopy(bBuffer, (short)(offset + 2), ArrayMsgOrURL, (short)0, (short)(ArrayMsgOrURL.length-1));
				else
					Util.arrayCopy(bBuffer, (short)(offset + 2), ArrayMsgOrURL, (short)0, (short)(len + 1));
					
			}
			else
			{
				Util.arrayCopy(bBuffer, (short)(offset + 1), ArrayMsgOrURL, (short)0, (short)(len + 1));
			}
			
			//serviceDisplayText(mainMsg, (short)0, (short) 170);
			
		}

	}

	private boolean launchBrowser() throws ToolkitException {
		try {
			ProactiveHandler ph = ProactiveHandlerSystem.getTheHandler();
			//ProactiveHandler ph = ProactiveHandler.getTheHandler();
			
			ph.init(PRO_CMD_LAUNCH_BROWSER, (byte) 0x00, DEV_ID_UICC);
			//ph.init(PRO_CMD_LAUNCH_BROWSER, (byte) 0x00, DEV_ID_ME);

			short nURLLen = Util.makeShort((byte)0x00, (byte) URL[(short)0]);
			ph.appendTLV(TAG_URL, URL, (short) 1, nURLLen);
			
			ph.send();

			return true;
			
		} catch (ToolkitException ignore) {
			fWriteLogShort((short)10, (byte)ignore.getReason());
			return false;
		}
	}

	/**
	 * @param message
	 * @return
	 */
	private byte fShowDisplayTextPopUp(byte[] message)
	{
		//try {
			
		ProactiveHandler proHdlr = ProactiveHandlerSystem.getTheHandler();
    	//ProactiveHandler proHdlr;
    	proHdlr = ProactiveHandlerSystem.getTheHandler();
    	//proHdlr = ProactiveHandler.getTheHandler();

		// Display Text - Wait for user to clear message"
		//ETSI 102.223: 0x81 = "Wait for user to clear message" and "High Priority"
		//proHdlr.initDisplayText((byte) 0x81, DCS_8_BIT_DATA, message, (short) 1, (short) Util.makeShort((byte)0x00, message[(short)0]));

		//UPDATED 2021-10-22 BECAUSE OF LONG DISPLAY TEXT
		//proHdlr.initDisplayText((byte) 0x81, DCS_8_BIT_DATA, message, (short) 1, (short) Util.makeShort((byte)0x00, message[(short)0]));
    	short nLen =  (short) Util.makeShort((byte)0x00, (byte)message[(short)0]);

    	//proHdlr.initDisplayText((byte) 0x81, DCS_8_BIT_DATA, message, (short)1, (short)nLen);
		proHdlr.initDisplayText((byte) 0x81, DisplayText_DCS_BIT_DATA, message, (short)1, (short)nLen);
		
		//OTA F0 or Timer activated
		return proHdlr.send();

		/*
		} catch (ToolkitException ignore) {
			return (byte)ignore.getReason();
		}
		*/
	}
	
	/**
	 * @param res
	 */
	private void fProcessRESAndSendSMS(byte res, byte bCmd1, byte bCmd2, boolean bAddIMEI)
	{
		short n=(short)0;
		
		if (res == RES_CMD_PERF)
		{
			//ETSI 102.223: '00' = Command performed successfully;
			transByteArray[N_NORESPONSEUSER_OFFSET] = (byte)RES_CMD_PERF;
			generalBuffer[(short)n++]=b31;

			//fnsendSms((byte)b1);
		}
		else
		{
			//b0[(short)(b0.length-1)]=res;
			//fnsendSms(b0, (short)0, (short)b0.length);
			transByteArray[DISPLAY_RES_OFFSET] = b30;
			transByteArray[DISPLAY_RES2_OFFSET] = res;
		
			generalBuffer[(short)n++]=transByteArray[DISPLAY_RES_OFFSET];
			generalBuffer[(short)n++]=transByteArray[DISPLAY_RES2_OFFSET];
				
		}

		/* NOT USED PER NOW
		Util.arrayCopy(COTA, (short)0, generalBuffer, n, (short)COTA.length);
		n+=(short)COTA.length;
		
		if(bCmd1!=(byte)bNULL)
		{
			generalBuffer[(short)n++]=bCmd1;
			generalBuffer[(short)n++]=bCmd2;
		}
		*/
		
		if(bAddIMEI)
		{
			Util.arrayCopy(IMEIOld, (short)0, generalBuffer, (short)n, (short)IMEIOld.length);
			n+=(short)IMEIOld.length;
		}
		
		fnsendSms(generalBuffer, (short)0, n);
	}

	private void showMessage(boolean bPopUp, boolean bCallURL, byte[] message, byte bCmd1, byte bCmd2)
	{

		//OTA F4: Setting default value before Launch Browser directly
		byte resPopUp = RES_CMD_PERF;
		
		if(bPopUp && !bCallURL)
		{
			//THIS IS FOR ONLY SHOWING THE WELCOME MESSAGE
			resPopUp = fShowDisplayTextPopUp(message);
			fProcessRESAndSendSMS(resPopUp, bCmd1, bCmd2, false);
			return;
		}
		
		if(fLaunchBrowserCheck())
		{
			if(bPopUp)
			{
				resPopUp = fShowDisplayTextPopUp(message);
			}
			
			//if (res == RES_CMD_PERF && TerminalProfile.check(PROFILE_LAUNCH_BROWSER)) {
			if (resPopUp == RES_CMD_PERF) 
			{
				launchBrowser();
			}

			fProcessRESAndSendSMS(resPopUp, bCmd1, bCmd2, false);

			if (resPopUp != RES_CMD_PERF) 
			{
				//RES_CMD_NO_RESPONSE_USER = 0x12
				//RES >= 0x20 ERROR 
				if(bNoResponseUserRetries)
				{
					if(resPopUp == RES_CMD_NO_RESPONSE_USER || resPopUp >= (byte)0x20)
					{
						//nNoResponseUser++;
						transByteArray[N_NORESPONSEUSER_OFFSET]++;
						if(transByteArray[N_NORESPONSEUSER_OFFSET] < nNoResponseUserMax)
						{
							//serviceDisplayText(TIMER_VALUE_INIT, (short)0, (short)TIMER_VALUE_INIT.length);
							//Start Timer Again
							loadTimer();
						}
					}
				}
			}
	
		}
		else
		{
			/*
			//ADDED 2021-12-21
			//LAUCH BROWSER NOT SUPPORTED
			transByteArray[DISPLAY_RES_OFFSET] = b0;
			//'30' = Command beyond terminal's capabilities;
			transByteArray[DISPLAY_RES2_OFFSET] = (byte)0x30;
			
			//ADDED 2022-02-08
			generalBuffer[(short)0]=transByteArray[DISPLAY_RES_OFFSET];
			generalBuffer[(short)1]=transByteArray[DISPLAY_RES2_OFFSET];
			Util.arrayCopy(IMEIOld, (short)0, generalBuffer, (short)2, (short)IMEIOld.length);
			
			//SEND SMS: STATUS
			//fnsendSms(transByteArray, DISPLAY_RES_OFFSET, (short)2);
			fnsendSms(generalBuffer, (short)0, (short)((short)2+IMEIOld.length));
			*/
			
			//LAUCH BROWSER NOT SUPPORTED
			//'30' = Command beyond terminal's capabilities;
			fProcessRESAndSendSMS((byte)0x30, bCmd1, bCmd2, true);
		}
	}
	
	//CHECK WHETHER LAUNCH BROWSER IS SUPPORTED
	/**
	 * @return
	 */
	private boolean fLaunchBrowserCheck()
	{
		if(TerminalProfile.check(PROFILE_LAUNCH_BROWSER))
		//if(MEProfile.check(PROFILE_LAUNCH_BROWSER))
			return true;
		else
			return false;
	}
	/**
	 * @param smsData
	 * @return
	 */
	private boolean fnsendSms(byte smsData)
	{
		transByteArray[DISPLAY_RES_OFFSET] = smsData;
		return fnsendSms(transByteArray, DISPLAY_RES_OFFSET, (short)1);
	}
	/**
	 * @param sData
	 * @param nStart
	 * @param nLen
	 * @return
	 */
	private boolean fnsendSms(byte[] smsData, short nStart, short nLen)
    {
		/*
		try
		{
		*/
			ProactiveHandler proHdlr = ProactiveHandlerSystem.getTheHandler();
			//ProactiveHandler proHdlr = ProactiveHandler.getTheHandler();
			short j = (short)0;
	    	//short udloffset = (short)0;
	                    
	  	    //Build SMS TPDU
	  	    j = (short)0;
	  	    //MESSAGE TYPE INDICATOR (MTI) bits 0-1 = SMS SUBMIT MS to SC
		    //REJECT DUPLICATES (RD) bit 2 = ACCEPT DUPLICATES
		    //VALIDITY PERIOD FORMAT (VPF) bits 3-4=  TP-VP field present - relative format
		    //Status-Report-Indication (SRR) bit 5 - No Report
		    //USER DATA HEADER INDICATOR (UDHI) = No header
		    //REPLY PATH (RP) - Not Set
	  	    byteArray2[j++] = (byte)0x05;
	  	        
	  	    //MESSAGE REFERENCE (MR)
	  	    byteArray2[j++] = (byte)0x00;
	  	       	        
	  	    //*******DESTINATION ADDRESS (DA)******
	     	  	
	  	    Util.arrayCopyNonAtomic(baTPDA_Address, (short)0, byteArray2, (short)j, (short)sTPDALength);
	  	    	 
	  	    j+=sTPDALength;
	  	          	        
	  		//*******PROTOCOL IDENTIFIER (PID)****** 
	  		byteArray2[j++]=(byte)0x00;
	  	         
			//*******DATA CODING SCHEME (DCS)******
	  		//DCS = 7 bit Packed => 0x10 (1 = Class meaning: bits b0 and b1 indicate class)
	  		//DCS = 8 bits Packed => 0x04
			//byteArray2[j++]=(byte)0x00;
			byteArray2[j++]=(byte)0x04;
	  	  	
			//VALIDITY PERIOD (VP) 
			//byteArray2[j++]=(byte)0x01;
			
			//USER DATA LENGTH (UDL) and USER DATA
	
	        //LEN - Assuming it is less than 127
	  	          	      
	        //udloffset = j++;
	  	      
	        //UDL
	  	    //byteArray2[j++] = (byte)SMSBUfferLength;
			//byteArray2[j++] = (byte)(smsData[(short)0]);
			//byteArray2[j++] = (byte)0x01;
	  	    byteArray2[j++] = (byte)nLen;
			
			// proHdlr.initDisplayText((byte)0x81,(byte)0x04, byteArray2,(short)0,(short)(j+1));
			// proHdlr.send();  
	  	      	
	  	    //*******USER DATA HEADER******     
	  	    //j = Util.arrayCopyNonAtomic(HEADER, (short)0, byteArray2, (short)j, (short)HEADER.length);
	  	  			 
	  	    //j = Util.arrayCopyNonAtomic(smsData, (short)1, byteArray2, (short)j, (short)smsData[(short)0]);
	  	    //byteArray2[j++] = smsData;
	  	    Util.arrayCopyNonAtomic(smsData, (short)nStart, byteArray2, (short)j, nLen);
	  	    j+=nLen;
	  	    
			//proHdlr.init((byte)0x13,(byte)0x01,DEV_ID_NETWORK);
			proHdlr.init((byte)0x13,(byte)0x00,DEV_ID_NETWORK);
			//Qualifier 0x01 means that the data requires Packing by the ME
			//Qualifier 0x00 means that Packing is not required
	   	         	       
			//Alpha identifier
			//proHdlr.appendTLV(TAG_ALPHA_IDENTIFIER,SMS_ALPHA,(short)0,(short)SMS_ALPHA.length); //Aplha ID
	  	       	   
	  	    	
	  	    //Append SMS TPDU
			proHdlr.appendTLV((byte)0x0B,byteArray2,(short)0,j);
	
			//FOR DEBUGGING
			fWriteLogBeforeSENDSMS(byteArray2, (short)0, j);
	
			byte res = proHdlr.send();
			if(res == RES_CMD_PERF)
			{  	        	
	  	        return true;
	  	    }
	  	    else 
	  	    	return false;
		/*
		}
		catch(ToolkitException e)
		{
			return false;
		}
		*/
    }

	public void process(APDU apdu) throws ISOException {
		if (selectingApplet()) {
			return;
		}

		byte[] buffer = apdu.getBuffer();
        short offset = (short)0;

        //DEBUGGING
        //serviceDisplayText(buffer, (short)0, (short)4);
        
		switch (buffer[ISO7816.OFFSET_INS]) {
            
		case INS_GET_DATA:
		
				offset = Util.arrayCopyNonAtomic(HEADER, (short) 0, buffer, offset, (short) HEADER.length);
				offset = Util.arrayCopyNonAtomic(APP_HASH, (short) 0, buffer, offset, (short) APP_HASH.length);
				apdu.setOutgoingAndSend((short) 0, offset);
				break;
			
            case INS_GET_RESPONSE:
				
				//if (CLA_APP == buffer[ISO7816.OFFSET_CLA]) {
				
					bAPK_APDU_VAL = APK_APDU_RECEIVED;
	
					if(buffer[ISO7816.OFFSET_P1]==bFC)				
						//COPY VALUE P2 FROM 0x80 02 FC P2 to APK_APDU_VAL, when it is needed to be updated with specific value 
						bAPK_APDU_VAL = buffer[ISO7816.OFFSET_P2];

					offset = Util.arrayCopyNonAtomic(RESPUESTA, (short) 0, buffer, offset, (short) RESPUESTA.length);
					
					//RESET TEMPORARY COUNTER
					fRAMArrayClean(transByteArray, N_STATUSCOMMAND_APK_APDU);
					
					//ADD IMEI IN THE RESPONSE
					//offset = Util.arrayCopyNonAtomic(IMEI, (short) 0, buffer, offset, (short) IMEI.length);
					//offset+=(short)IMEI.length;
					
					apdu.setOutgoingAndSend((short) 0, offset);
					
				//} else {
				//	ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
				//}
					
				break;
	    
			case (byte)bFA:
				//ANSWER WHETHER THERE IS ANYTHING RECEIVED FROM OTA WITH 0XFA TO APK-ANDROID
				buffer[(short)offset]=bFAValue;
				apdu.setOutgoingAndSend((short) 0, (short)1);
				
				bFAValue = (byte)0x00;
				
				break;

			case (byte)bF8:
				//GET SAVED IMEI
				offset = Util.arrayCopyNonAtomic(IMEIOld, (short) 0, buffer, offset, (short) IMEIOld.length);
				offset = Util.arrayCopyNonAtomic(COTA, (short)0, buffer, offset, (short)COTA.length);
				apdu.setOutgoingAndSend((short) 0, offset);

				break;

			case (byte)bFE:
				//GET LOCI
				//NOT POSSIBLE TO RETRIEVE LOCI BECAUSE HANDLER IS NOT AVAILABLE
		    	//fGetLocalInformationLOCI(loci, (short)0);
				
				offset = Util.arrayCopyNonAtomic(loci, (short) 0, buffer, offset, (short) loci.length);
				offset = Util.arrayCopyNonAtomic(COTA, (short)0, buffer, offset, (short)COTA.length);
				apdu.setOutgoingAndSend((short) 0, offset);

				break;

			default:
				ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
				break;
		}
	}

	/**
	 * @return
	 */
	private boolean fSendSMSAPKApduStatus(boolean bAddCOTA)
	{
		short n=(short)0;

		//DEBUGGING
		//fWriteLog((short)10, IMEIOld, (short)0, (short)IMEIOld.length);

		Util.arrayCopy(IMEIOld, (short)0, generalBuffer, (short)n, (short)IMEIOld.length);
		n+=(short)IMEIOld.length;

		//VARIABLE SET AFTER APK CALLED APPLET
		generalBuffer[(short)n++]=bAPK_APDU_VAL;
		
		//COUNTER FOR STATUS COMMANDS EXECUTED TAKING INTO ACCOUNT WHETHER APK HAS SENT APDU TO APPLET OR NOT
		generalBuffer[(short)n++]=transByteArray[N_STATUSCOMMAND_APK_APDU];
		generalBuffer[(short)n++]=transByteArray[(short)(N_STATUSCOMMAND_APK_APDU+1)];
				
		if(bAddCOTA)
		{
			Util.arrayCopy(COTA, (short)0, generalBuffer, n, (short)COTA.length);
			n+=(short)COTA.length;
		}
		
		return fnsendSms(generalBuffer, (short)0, n);
	}

	/**
	 * @param bAddCOTA
	 * @return
	 */
	/*
	private boolean fSendSMSLoci(boolean bAddCOTA)
	{
		return fSendSMSWithCOTAVer(bAddCOTA, loci, (short)0, (short)loci.length);
	}
	*/
	/**
	 * @param bAddCOTA
	 * @param bAddMaxCounter
	 * @param MaxCounter
	 * @return
	 */
	private boolean fSendSMSLoci(boolean bAddCOTA, boolean bAddMaxCounters)
	{
		short n=(short)0;
		
		Util.arrayCopy(loci, (short)0, generalBuffer, (short)n, (short)loci.length);
		n+=(short)loci.length;
		
		if(bAddMaxCounters)
		{
			fShortToByte(MAX_COUNTER_GETLOCI, generalBuffer, n);
			n+=(short)2;
			fShortToByte(MAX_COUNTER_GETLOCI_APK, generalBuffer, n);
			n+=(short)2;
		}

		return fSendSMSWithCOTAVer(bAddCOTA, generalBuffer, (short)0, (short)n);
	}
	
	/**
	 * @param nVal
	 * @param bOut
	 * @param nOutStart
	 */
	void fShortToByte(short nVal, byte[] bOut, short nOutStart)
	{
		bOut[(short)nOutStart]=(byte)((short)0x00FF & (nVal >> (short)8));
		bOut[(short)(nOutStart+1)]=(byte)((short)0x00FF & nVal);
	}


	/**
	 * @param bAddCOTA
	 * @param buffer
	 * @param nStart
	 * @param nLen
	 * @return
	 */
	private boolean fSendSMSWithCOTAVer(boolean bAddCOTA, byte buffer[], short nStart, short nLen)
	{
		short n=(short)0;

		Util.arrayCopy(buffer, (short)nStart, generalBuffer, (short)n, (short)nLen);
		n+=(short)nLen;

		if(bAddCOTA)
		{
			Util.arrayCopy(COTA, (short)0, generalBuffer, n, (short)COTA.length);
			n+=(short)COTA.length;
		}
		
		return fnsendSms(generalBuffer, (short)0, n);
	}
	
    /**
     * @param sSource
     * @param nSourceStart
     * @param nSourceLen
     * @param sTPDAOK
     * @param nTPDAOKStart
     * @return
     */
    private boolean fValidateTPDASourceToDestination(byte[] sSource, short nSourceStart, short nSourceLen, byte[] sTPDAOK, short nTPDAOKStart)    {
        
    	//Example: 0x44 0C 81 21 43 65 87 09 21 7F F6 00 00 00 00 00 00 00 21 02 70
    	//TPDA:       0x0C 81 21 43 65 87 09 21
    	
    	/*
    	 * According 3GPP 23.040:
			PID (Protocol ID) = 7F
			7F = 0111 1111 => SIM Data Download

			According 3GPP 23.038:
			DCS (Data Coding Scheme) = F6
			F6 = 1111 0110 
			=> 1111 = Language unspecified
			=> 0110 = Class 2 (U)SIM specific message. 
    	 */
    	
    	short n=(short)0;
    	short m=(short)0;
    	short i=(short)0; //FOR NOT VALIDATING TON/NPI
    	boolean bLeave=false;

    	//+1 BECAUSE OF 0x44 IN THE EXAMPLE
    	n=(short)(nSourceStart+1);
    	m=(short)nTPDAOKStart; //For TPDA, first record has the amount of bytes for coding
    	while(!bLeave && n<(short)(nSourceStart + nSourceLen) && n < (short)sSource.length && m<(short)sTPDAOK.length )
    	{
    		
    		//NOT TO VALIDATE TON/NPI SUCH AS 0x81, 0xA1, 0x01, ETC.
    		if(i!=(short)1)
    		{
	    		byte bF6 = (byte) (sSource[(short)(n+1)] & (byte)0xF6);
	    		
	    		//COMPARE TO 0x7F F6 for leaving while
	    		if((sSource[n]==(byte)0x7F && bF6 == (byte)0xF6)
	    				|| (sSource[n]==(byte)0x00 && bF6==(byte)0xF6))
	    			bLeave=true;
	    		else
	    		{
		    		if(sSource[n]!=sTPDAOK[m])
		    		{
		    	    	//serviceDisplayText(sSource, nSourceStart, nSourceLen);
		    			return false;
		    		}
	    		}
    		}
    		
    		n++;
    		m++;
    		i++;
    	}
    	
    	//serviceDisplayText(sTPDAOK, nTPDAOKStart, (short)sTPDAOK.length);
    	
    	return true;
    }

    /**
     * @param bArray
     * @param nStart
     * @param sTime
     */
    boolean fTimePrepare(byte[] bArray, short nStart, byte[] sTime)
    {
    	short n=(short)0;
    	short nMax = (short)3;
    	
    	if(fTimerNull(bArray, nStart))
    		return false;

    	for(n=(short)0; n<nMax && (short)(nStart+nMax)<(short)bArray.length; n++, nStart++)
    	{
    		sTime[n]=bArray[nStart];
    	}
    	
    	/*
    	fTimePrepareVal(bArray, nStart++, sTime, n++);
    	fTimePrepareVal(bArray, nStart++, sTime, n++);
    	fTimePrepareVal(bArray, nStart++, sTime, n++);
    	*/
    	
    	return true;
    }

    /**
     * @param bArray
     * @param nStart
     * @return
     */
    boolean fTimerNull(byte[] bArray, short nStart)
    {
    	//COMPARE TIMER TO NULL VALUE
    	if(Util.arrayCompare(bArray, (short)nStart, TIMER_VALUE_NULL, (short)0, (short)TIMER_VALUE_NULL.length)==(byte)0)
			return true;

    	return false;
    }
    
    /**
     * @param bArray
     * @param nStart
     * @param sTime
     * @param nPos
     */
    /*
    void fTimePrepareVal(byte[] bArray, short nStart, byte[] sTime, short nPos)
    {
    	byte bVal = (byte)0;
    	
    	if(nStart<(short)bArray.length)
    	{
    		bVal=fReverseByte(bArray[nStart]);
    		//if(bVal > (byte) 0)
    			sTime[nPos] = bVal;
    	}
    	
    }
     */
	//REVERSE 4 BITS
	/**
	 * @param bVal
	 * @return
	 */
    /*
	byte fReverseByte(byte bVal)
	{
	      byte b, b1, b2;

	      b=bVal;
	      b1=(byte)((byte)0xF0 & (b << (short)4));
	      b2=(byte)((byte)0x0F & (b >> (short)4));
	      b=(byte)(b1 & b2);
	      
	      return b;
	}
    */
	/**
	 * @param buffer
	 * @param nStart
	 * @return
	 */
    /* */
	public boolean fGetLocalInformationLOCI(byte[] buffer, short nStart) 
	{
		return fGetLocalInformation((byte)0x00, buffer, nStart);
	}
	/* */
	/**
	 * @param buffer
	 * @param nStart
	 * @param bAddLaunchBrowserSupported
	 * @return
	 */
	public boolean fGetLocalInformationIMEI(byte[] buffer, short nStart, short nAddLaunchBrowserSupported) 
	{
		boolean bReturn = fGetLocalInformation((byte)0x01, buffer, nStart);
		
		if(nAddLaunchBrowserSupported>=(short)0)
		{
			if(fLaunchBrowserCheck())
				//b1 = 0x31
				buffer[(short)(nStart + nAddLaunchBrowserSupported)]=b31;
			else
				//b0 = 0x30
				buffer[(short)(nStart + nAddLaunchBrowserSupported)]=b30;
		}
		
		return bReturn;
	}
	/**
	 * @param qualifier
	 * @param buffer
	 * @param nStart
	 * @return
	 */
	public boolean fGetLocalInformation(byte qualifier, byte[] buffer, short nStart) {
		byte retCode;
		short len = (short) 0;

		//FORMATING THE APDU
	
		try
		{
			ProactiveHandler proHandler = ProactiveHandlerSystem.getTheHandler();
			//ProactiveHandler proHandler = ProactiveHandler.getTheHandler();
	
			proHandler.init(ToolkitConstants.PRO_CMD_PROVIDE_LOCAL_INFORMATION, qualifier, ToolkitConstants.DEV_ID_UICC);
			//proHandler.init(ToolkitConstants.PRO_CMD_PROVIDE_LOCAL_INFORMATION, qualifier, ToolkitConstants.DEV_ID_ME);
			
			retCode = proHandler.send();
		
			//SENDING THE APDU AND GETTING THE RESPONSE
			ProactiveResponseHandler responseHandler = ProactiveResponseHandlerSystem.getTheHandler();
			//ProactiveResponseHandler responseHandler = ProactiveResponseHandler.getTheHandler();
			retCode = responseHandler.findTLV(ToolkitConstants.TAG_DEVICE_IDENTITIES, (byte) 1);
	
			//Provide Local Information - LOCI
			if(qualifier == (byte) 0x00)
			{
				//0x13 is the Tag for the LOCI in the Response
				retCode = responseHandler.findTLV(ToolkitConstants.TAG_LOCATION_INFORMATION, (byte) 1);
			}
			//Provide Local Information - IMEI
			if(qualifier == (byte) 0x01)
			{
				//0x14 is the Tag for the IMEI in the Response
				retCode = responseHandler.findTLV(ToolkitConstants.TAG_IMEI, (byte) 1);			
			}
			
			if ((retCode == ToolkitConstants.TLV_FOUND_CR_SET) || (retCode == ToolkitConstants.TLV_FOUND_CR_NOT_SET)) 
			{
				len = responseHandler.getValueLength();
				if (len > (short) 0 && len <= (short) buffer.length)
				{
					responseHandler.copyValue((short) 0, buffer, nStart, len);
					//if(Util.arrayCompare(transByteArray, TEMP_IMEI_OFFSET, buffer, nStart, len) != (byte)0x00){
					//	Util.arrayCopyNonAtomic(transByteArray, TEMP_IMEI_OFFSET, buffer, nStart, len);
					//}
				
				}
			}		

		}
		catch (ToolkitException ignore) {
			return false;
		}
		
		return true;
	}

	public boolean fURLReplaceWithMCC(byte[] sURL, byte[] sMCC)
	{
		return fURLReplaceWithMCC_MNC(true, sURL, sMCC);
	}
	public boolean fURLReplaceWithMNC(byte[] sURL, byte[] sMNC)
	{
		return fURLReplaceWithMCC_MNC(false, sURL, sMNC);
	}
	//REPLACE URL 'mcc=' with the actual MCC (Mobile Country Code)
	//REPLACE URL 'mnc=' with the actual MNC (Mobile Network Code)
	public boolean fURLReplaceWithMCC_MNC(boolean bMCC, byte[] sURL, byte[] sBuffer)
	{
		short n;
		boolean bFound=false;
		
		for(n=(short)1; n < (short)sURL.length && (short)sBuffer.length >= (short)3 && !bFound; n++)
		{
			if(sURL[(short)n]==(byte)0xFF)
				return false;
			else
			{
				if(((short)(n+(short)7))<(short)sURL.length)
				{

					if((sURL[n]  ==(byte)'m' || sURL[n]  ==(byte)'M')
							&& (sURL[(short)(n+(short)2)]==(byte)'c' || sURL[(short)(n+(short)2)]==(byte)'C')
							&&  sURL[(short)(n+(short)3)]==(byte)'='
							)
					{
						if(bMCC)
						{
							if((sURL[(short)(n+(short)1)]==(byte)'c' || sURL[(short)(n+(short)1)]==(byte)'C'))
								bFound=true;
						}
						else
						{
							if((sURL[(short)(n+(short)1)]==(byte)'n' || sURL[(short)(n+(short)1)]==(byte)'N'))
								bFound=true;
						}
					}
				}
				else
					return false;

				if(bFound)
				{
					//FOUND THE PATTERN 'mcc=' or 'mnc='
					n+=(short)4;
					sURL[n++]=sBuffer[(short)0];
					sURL[n++]=sBuffer[(short)1];
					sURL[n]  =sBuffer[(short)2];
					
					return true;
				}
			}
		}
		
		return false;
	}


	private void fRAMArrayClean(byte[] bArray, short nStart)
	{
		bArray[nStart]=(byte)0x00;
		bArray[(short)(nStart+1)]=(byte)0x00;
	}
	
	private short fRAMArraySumUpShort(byte[] bArray, short nStart)
	{
		short nValue = Util.makeShort(bArray[nStart], bArray[(short)(nStart+1)]);
		if(nValue > (short)0x7FFF)
			nValue=(short)0;
		else
			nValue++;
			
		fromShortToBytesIntoByteArray(nValue, bArray, nStart);
		return nValue;
	}
	
	void fromShortToBytesIntoByteArray(short nValue, byte[] bArray, short nStartbArray)
	{
	      short n;

	      n=nValue;
	      bArray[(short)nStartbArray++]=(byte)((short)0x00FF & (n >> (short)8));
	      bArray[(short)nStartbArray]=(byte)((short)0x00FF & nValue);
	}

	//**************************************************************************
	//FOR DEBUGGING
	/* */
	private static FileView usimFile;
	//private static SIMView usimFile;
	private final short MF = 0x3F00;
    private byte[] SMS  = new byte[(short)176];
    private byte[] generalBufferLog  = new byte[(short)10];
	private final short EF_SMS = 0x6F3C;
	private final short EF_TELECOM = 0x7F10;
	/* */
	/* 
	 * RECORDS: 
	 * 1 = SMSPP RECEIVED - TPDA
	 * 2 = SMSPP RECEIVED = DATA
	 * 3 = SMS SENT
	 * 4 = GENERAL
	 * */

	/* */
	//WRITE LOG FILE
	private boolean fWriteLogAfterSMSPP_TPDA(byte[] sBuffer, short nFrom, short nLen)
	{
		return fWriteLog((short) 1, sBuffer, nFrom, nLen);
	}
	private boolean fWriteLogAfterSMSPP_Data(byte[] sBuffer, short nFrom, short nLen)
	{
		return fWriteLog((short) 2, sBuffer, nFrom, nLen);
	}
	private boolean fWriteLogBeforeSENDSMS(byte[] sBuffer, short nFrom, short nLen)
	{
		return fWriteLog((short) 3, sBuffer, nFrom, nLen);
	}
	private boolean fWriteLogShort(short nVal)
	{
		return fWriteLogShort((short) 4, nVal);
	}
	private boolean fWriteLogShort(short nRecord, short nVal)
	{
		fShortToByte(nVal, generalBufferLog, (short)0);
		
		return fWriteLog(nRecord, generalBufferLog, (short)0, (short)2);
	}
	private boolean fWriteLogByte(byte nVal)
	{
		return fWriteLogByte((short) 4, nVal);
	}
	private boolean fWriteLogByte(short nRecord, byte nVal)
	{
		generalBufferLog[(short)0]=nVal;
		return fWriteLog(nRecord, generalBufferLog, (short)0, (short)1);
	}
	private boolean fWriteLogBoolean(boolean nVal)
	{
		return fWriteLogBoolean((short) 4, nVal);
	}
	private boolean fWriteLogBoolean(short nRecord, boolean nVal)
	{
		if(nVal)
			//b1 = 0x31
			generalBufferLog[(short)0]=(byte)0x31;
		else
			//b0 = 0x30
			generalBufferLog[(short)0]=(byte)0x30;
			
		return fWriteLog(nRecord, generalBufferLog, (short)0, (short)1);
	}
	private boolean fWriteLogGeneral(byte[] sBuffer, short nFrom, short nLen)
	{
		return fWriteLog((short) 4, sBuffer, nFrom, nLen);
	}
	private boolean fWriteLog(short nRecord, byte[] sBuffer, short nFrom, short nLen)
	{
	
		try{

			usimFile.select((short) MF);
			usimFile.select((short) EF_TELECOM);
			usimFile.select((short) EF_SMS);

			Util.arrayFillNonAtomic(SMS, (short)0, (short) SMS.length, (byte)0x00);
			usimFile.updateRecord(nRecord, (byte)0x04, (short)0, SMS, (short)0, (short)SMS.length);
			
			if(nLen>(short)0 && nFrom >= (short)0)
			{
				Util.arrayFillNonAtomic(SMS, (short)0, (short) SMS.length, (byte)0x00);
				if(nLen <= 127)
				{
					Util.arrayCopyNonAtomic(sBuffer, nFrom, SMS, (short)1, nLen);
					SMS[(short)0]=(byte)nLen;
				}
				else
				{
					if((short)(nLen-nFrom)>=(short)(SMS.length-2))
						Util.arrayCopyNonAtomic(sBuffer, nFrom, SMS, (short)2, (short)(SMS.length-2-nFrom));
					else		
						//Util.arrayCopyNonAtomic(sBuffer, nFrom, SMS, (short)2, (short)(SMS.length-2));
						Util.arrayCopyNonAtomic(sBuffer, nFrom, SMS, (short)2, nLen);
					
					fShortToByte(nLen, SMS, (short)0);
				}
				
			}
			
			//RECORD 1 = SMSPP INCOMING - TPDA
			//RECORD 2 = SMSPP INCOMING - DATA
			//RECORD 3 = SMS SENT
			//usimFile.updateBinary((short)0, sBuffer, nFrom, nLen);
			//0x04 = ABSOLUTE MODE
			usimFile.updateRecord(nRecord, (byte)0x04, (short)0, SMS, (short)0, (short)SMS.length);

			Util.arrayFillNonAtomic(SMS, (short)0, (short) SMS.length, (byte)0x00);
			
			return true;

		//} catch (UICCException e) {
		} catch (Exception e) {
			//fDebuggingShort(e.getReason());
			return false;
		}
		
	}

	//***************************************************************************
	//***************************************************************************************************************

}