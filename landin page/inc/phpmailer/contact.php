<?php
/*
| -------------------------------------------------------------------------------------
|	Author: Templines
|	Contact Us send mail
| -------------------------------------------------------------------------------------
| This is just an example how to process contact form
| This template does not support any issue of this file
| -------------------------------------------------------------------------------------
 */
 
/* --------------------------------------------
	Your E-Mail Configurations
-------------------------------------------- */
function init_mail()
{
	$host = '';			// Set the hostname of the mail server
	$port = 25;						// Set the SMTP port number - likely to be 25, 465 or 587
	$username = '';	// Username to use for SMTP authentication
	$password = '';			// Password to use for SMTP authentication

	//Create a new PHPMailer instance
	$mail = new PHPMailer;
	$mail->CharSet = 'utf-8';
	$mail->Debugoutput = 'error_log';
	$mail->IsMail();
	// Tell PHPMailer to use SMTP
	//$mail->IsSMTP();
	/* SMTP Debug Mode:
		0 : Disabled
		1 : Client Message
		2 : Server and Client Message
	*/
	////$mail->SMTPDebug  = 1;
	//$mail->Host = $host;
	//$mail->Port = $port;
	//Whether to use SMTP authentication
	//$mail->SMTPAuth   = true;
	//$mail->Username   = $username;
	//$mail->Password   = $password;
	
	return $mail;
}

$configs['to'] = 'prestamaster@gmail.com';			// who should recieve the contact form data
$configs['to_name'] = 'prestamaster';			// who should recieve the contact form data
$configs['from'] = 'noreply@gmail.com';		// Set who the thanks message is to be sent from
$configs['from_name'] = 'Tofito';
$configs['thanks_subject'] = 'Thanks';			// Set the subject line of thank message
// Set HTML Content to send
$configs['thanks_body'] = '<h3>Thank you, </h3><p>this message means that we recieved your comment.</p>';
 
 // Get Form data
$configs['name'] = isset($_POST['name']) ? trim($_POST['name']) : null;
$configs['email'] = isset($_POST['email']) ? trim($_POST['email']) : null;
$configs['subject'] = isset($_POST['subject']) ? trim($_POST['subject']) : null;
$configs['message'] = isset($_POST['message']) ? trim($_POST['message']) : null;

// hold errors
$err = array();

// a bit validation
if ( ! isset($configs['name']) || empty($configs['name']) )
	$err[] = array('name'=>'Please enter your name.');
	
if ( ! isset($configs['message']) || empty($configs['message']) )
	$err[] = array('message'=>'Please leave your message for us.');
	
if ( ! filter_var($configs['email'], FILTER_VALIDATE_EMAIL) )
	$err[] = array('email'=>'Please enter valid e-mail so we can reply you.');

	
// if we had error, return them and exit
if ( count($err) )
{
	die( json_encode($err) );
}
// there was no error yet, so we process
else
{
	// include phpmailer class
	require './class.phpmailer.php';
	
	$mail = init_mail();
	$mail->SetFrom( $configs['email'], $configs['name'] );
	//Set who the message is to be sent to
	$mail->AddAddress( $configs['to'], $configs['to_name'] );
	$mail->Subject = $configs['subject'];
	$mail->MsgHTML($configs['message']);

	//Send the message, check for errors
	if ( ! $mail->Send() )
	{
		die ( json_encode( array( 'fail' => 'There was a problem due sending email: ' . $mail->ErrorInfo ) ) );
	}
	else
	{
		echo json_encode(array('success'=>true));
		
		unset( $mail );
		$mail = init_mail();
		
		// Now we send a message of thanks to the user
		$mail->SetFrom( $configs['from'], $configs['from_name'] );
		//Set an alternative reply-to address
		$mail->AddReplyTo( $configs['from'], $configs['from_name'] );
		$mail->AddAddress( $configs['email'], $configs['name'] );
		$mail->Subject = $configs['thanks_subject'];
		$mail->MsgHTML($configs['thanks_body']);
		$mail->Send();
		
		die();
	}
	
}

?>