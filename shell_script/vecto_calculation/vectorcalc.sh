#!/bin/sh

# @author: Konstantin Opora
#
# performs vector calculations :
#	(
#	Addition (ADD), 
# 	Subtraction(SUB)
#   Vectorproduct(VEC),
# 	calarmulitplication(SMUL), 
# 	Scalarproduct(SPROD)
#	)
#
# if calculation is successful, return result and exit code = 0 
# else prints ERROR Message and help, also returns exit code > 0

# Error List:
#	- "ERROR: exit code 1 -> check syntax of Operator to invoke right method"
#			-> Wrong Syntax in the "operand operation operand" expresion 
#			  | includes wrong spelling of operator e.g : adD ...
#	- "ERROR: exit code 2 -> Unknown Operator"
#			-> Given Operator is unkown
#	- "ERROR: exit code 3 -> No extra parameter after (-h / --help)"
#			-> A call for help cannot be followed by any further parameters
#	- "ERROR: exit code 4 -> SPROD has to be the last operator"
#			-> SPROUD was not the last given Operator

#*contains help text
usage() {
	cat << EOT
Usage:
vectorcalc.sh -h | vectorcalc.sh --help

  prints this help and exits.

vectorcalc.sh V1 V2 V3 [ OP W1 W2 W3 | SMUL N ] ... [ SPROD X1 X2 X3 ]

  provides a simple calculator for vectors in three dimensions using an infix notation.
  A valid call consists of a first vector and an arbitrary long (maybe empty) list of "pairs".
  Every pair consists of either an operation OP and a vector or the operation SMUL and a number,
  in each case producing a vector again. Optionally, there is a terminal operation SPROD and a
  vector, producing just a number. If there is no operation at all, the operation is assumed to 
  be the identity (producing the first vector).

  Vi, Wi, Xi and N are treated as integer numbers, i=1,2,3.

  Beginning with the second operation, the role of V1, V2 and V3 is the result of
  the previous operation.

  OP is one of:

    ADD - componentwise addition of two vectors
    SUB - componentwise subtraction of two vectors (second from first)
    VEC - vector product (or: cross product) of two vectors

  SMUL means the (scalar) multiplication of a vector by a number, SPROD consists of
  calculating the standard scalar product of two vectors.
EOT
}

# @method: adds two Vectors and returns the result in stdout
# @param: $1 - $3 first Vector
# @param: $4 - $6 second Vector
function_add() {
	local f=$(( $1+$4 ))
	local s=$(( $2+$5 ))
	local t=$(( $3+$6 ))
	echo "$f $s $t"
}

# @method: subtracts two Vectors and returns the result in stdout
# @param: $1 - $3 first Vector
# @param: $4 - $6 second Vector
function_sub() {
	local f=$(( $1-$4 ))
	local s=$(( $2-$5 ))
	local t=$(( $3-$6 ))
	echo "$f $s $t"
}

# @method: scalar multiplicats a Vector and a number and returns the result in stdout
# @param: $1 - $3 first Vector
# @param: $4 : Number for scalar multiplication
function_smul() {
	local f=$(( $1*$4 ))
	local s=$(( $2*$4 ))
	local t=$(( $3*$4 ))
	echo "$f $s $t"
}

# @method: forms the scalatprodukt of two vectors and returns the result in stdout
# @param: $1 - $3 first Vector
# @param: $4 - $6 second Vector
function_sprod(){
	echo $(( $1*$4 + $2*$5 + $3*$6 ))
}

# @method: forms the vectorproduct of two vectors and returns the result in stdout
# @param: $1 - $3 first Vector
# @param: $4 - $6 second Vector
function_vec() {
	local f=$(( $2*$6 - $3*$5))
	local s=$(( $3*$4 - $1*$6))
	local t=$(( $1*$5 - $2*$4))
	echo "$f $s $t"
}

# carry for the exitcode
EXITCODE=0

# prints usage if -h or --help is invoked and returns exit code=0 
# if more parameter after -h / --help are given -> ERROR exit code=3
if [ "X$1" = "X-h" ] || [ "X$1" = "X--help" ]
	then 
		if [ $# -eq 1 ]
			then 
				usage
				exit 0
		fi

		EXITCODE=3
fi

# checks if :
# 	-> ./vectorcalc.sh 1 (not even a full vector)
if [ $# -lt 3 ] && [ "$EXITCODE" -eq 0 ]
	then
		EXITCODE=1
fi

# sets RES to the first Vector
RES="$1 $2 $3"

# calculates results repentlessly until count of parameters less or equal to 3
# case $4 in (identiefies Operator)
#	SMUL -> Skalarmultiplication
#	ADD -> Addition
#	SUB -> Subtraction
#	SPROD -> Skalarproduct
#	VEC -> VectorProduct
#	[sS]... -> if OP is correct but syntactically erraneous -> prints ERROR MEssage and exit code = 1
#	* -> Any other case will be detected as an unknown Operator, prints ERROR Message and exit code = 2

while [ $# -gt 3 ] && [ "$EXITCODE" -eq 0 ]
do	
	# check if we have any of the cases:
	#	-> ./vectorcalc.sh 1 1 1 SMUL 1 (Second Vektor is not complete will using SMUL)
	#	-> ./vectorcalc.sh 1 1 1 ADD 2 (Second Vektor is not complete with any other Operation)
	if ( [ "X$4" = "XSMUL" ] && [ $# -lt 5 ] ) || ( [ "X$4" != "XSMUL" ] && [ $# -lt 7 ] )
		then
			EXITCODE=1
			continue
	fi
    case $4 in	
		SMUL)
			RES=$(function_smul $RES $5)
			;;
		ADD)	
			RES=$(function_add $RES $5 $6 $7)
			;;
		SUB)
			RES=$(function_sub $RES $5 $6 $7)
			;;
		SPROD)	
			RES=$(function_sprod $RES $5 $6 $7)
			if [ $# -gt 7 ]
			then
				EXITCODE=4
			fi
			;;
		VEC)
			RES=$(function_vec $RES $5 $6 $7)
			;;
		[sS][mM][uU][lL] | [aA][dD][dD] | [sS][uU][bB] | [sS][pP][rR][oO][dD] | [vV][eE][cC])
			EXITCODE=1
			;;
		*)	
			# exit Code 2 = unknon operator
			EXITCODE=2
			;;
	esac

	# detemines how often to shift according to the Operator
	OP=$4
	if [ "X$OP" = "XSMUL" ]
		then 
			shift 2
		else
			shift 4
	fi

done

case "$EXITCODE" in
	1)
		echo "ERROR: exit code 1 -> check syntax of Operator to invoke right method" 1>&2;;
	2)
		echo "ERROR: exit code 2 -> Unknown Operator" 1>&2;;
	3)
		echo "ERROR: exit code 3 -> No extra parameter after $1" 1>&2;;
	4)
		echo "ERROR: exit code 4 -> SPROD has to be the last operator" 1>&2;;

	esac

if [ "$EXITCODE" -ne 0 ]
	then
		usage 1>&2
		exit $EXITCODE
fi

echo $RES
