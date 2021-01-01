<html>
	
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Delete Stack</title>

<form action="" method="post" >
	<input type="text" name="f" />
	<input type="submit" value="Delete" />
	<?php
		if(isset($_POST['f']) && $_POST['f']){
			unlink($_POST['f']);
			echo "</br>done..!!";
		}
	?>
</form>

</html>
