	.text
	.file	"builtin.c"
	.globl	print                   # -- Begin function print
	.p2align	2
	.type	print,@function
print:                                  # @print
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 16
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	lw	a1, -16(s0)
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	call	printf
	lw	s0, 8(sp)
	.cfi_def_cfa sp, 16
	lw	ra, 12(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 16
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end0:
	.size	print, .Lfunc_end0-print
	.cfi_endproc
                                        # -- End function
	.globl	println                 # -- Begin function println
	.p2align	2
	.type	println,@function
println:                                # @println
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 16
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	lw	a1, -16(s0)
	lui	a0, %hi(.L.str.1)
	addi	a0, a0, %lo(.L.str.1)
	call	printf
	lw	s0, 8(sp)
	.cfi_def_cfa sp, 16
	lw	ra, 12(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 16
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end1:
	.size	println, .Lfunc_end1-println
	.cfi_endproc
                                        # -- End function
	.globl	printInt                # -- Begin function printInt
	.p2align	2
	.type	printInt,@function
printInt:                               # @printInt
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 16
	.cfi_def_cfa s0, 0
	sw	a0, -12(s0)
	lw	a1, -12(s0)
	lui	a0, %hi(.L.str.2)
	addi	a0, a0, %lo(.L.str.2)
	call	printf
	lw	s0, 8(sp)
	.cfi_def_cfa sp, 16
	lw	ra, 12(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 16
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end2:
	.size	printInt, .Lfunc_end2-printInt
	.cfi_endproc
                                        # -- End function
	.globl	printlnInt              # -- Begin function printlnInt
	.p2align	2
	.type	printlnInt,@function
printlnInt:                             # @printlnInt
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 16
	.cfi_def_cfa s0, 0
	sw	a0, -12(s0)
	lw	a1, -12(s0)
	lui	a0, %hi(.L.str.3)
	addi	a0, a0, %lo(.L.str.3)
	call	printf
	lw	s0, 8(sp)
	.cfi_def_cfa sp, 16
	lw	ra, 12(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 16
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end3:
	.size	printlnInt, .Lfunc_end3-printlnInt
	.cfi_endproc
                                        # -- End function
	.globl	getString               # -- Begin function getString
	.p2align	2
	.type	getString,@function
getString:                              # @getString
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 16
	.cfi_def_cfa s0, 0
	addi	a0, zero, 1024
	mv	a1, zero
	call	malloc
	sw	a0, -16(s0)
	lw	a1, -16(s0)
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	call	__isoc99_scanf
	lw	a0, -16(s0)
	lw	s0, 8(sp)
	.cfi_def_cfa sp, 16
	lw	ra, 12(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 16
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end4:
	.size	getString, .Lfunc_end4-getString
	.cfi_endproc
                                        # -- End function
	.globl	getInt                  # -- Begin function getInt
	.p2align	2
	.type	getInt,@function
getInt:                                 # @getInt
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 16
	.cfi_def_cfa s0, 0
	lui	a0, %hi(.L.str.2)
	addi	a0, a0, %lo(.L.str.2)
	addi	a1, s0, -12
	call	__isoc99_scanf
	lw	a0, -12(s0)
	lw	s0, 8(sp)
	.cfi_def_cfa sp, 16
	lw	ra, 12(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 16
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end5:
	.size	getInt, .Lfunc_end5-getInt
	.cfi_endproc
                                        # -- End function
	.globl	toString                # -- Begin function toString
	.p2align	2
	.type	toString,@function
toString:                               # @toString
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -64
	.cfi_def_cfa_offset 64
	sw	ra, 60(sp)
	sw	s0, 56(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 64
	.cfi_def_cfa s0, 0
	sw	a0, -20(s0)
	lw	a0, -20(s0)
	bnez	a0, .LBB6_2
	j	.LBB6_1
.LBB6_1:
	addi	a0, zero, 2
	mv	a1, zero
	call	malloc
	sw	a0, -24(s0)
	lw	a0, -24(s0)
	addi	a1, zero, 48
	sb	a1, 0(a0)
	lw	a0, -24(s0)
	sb	zero, 1(a0)
	lw	a0, -24(s0)
	sw	a0, -16(s0)
	j	.LBB6_13
.LBB6_2:
	lw	a0, -20(s0)
	srli	a0, a0, 31
	sh	a0, -50(s0)
	lhu	a0, -50(s0)
	beqz	a0, .LBB6_4
	j	.LBB6_3
.LBB6_3:
	lw	a0, -20(s0)
	neg	a0, a0
	sw	a0, -20(s0)
	j	.LBB6_4
.LBB6_4:
	sh	zero, -52(s0)
	j	.LBB6_5
.LBB6_5:                                # =>This Inner Loop Header: Depth=1
	lw	a0, -20(s0)
	addi	a1, zero, 1
	blt	a0, a1, .LBB6_7
	j	.LBB6_6
.LBB6_6:                                #   in Loop: Header=BB6_5 Depth=1
	lw	a0, -20(s0)
	lui	a1, 419430
	addi	a1, a1, 1639
	mulh	a2, a0, a1
	srli	a3, a2, 31
	srli	a2, a2, 2
	add	a2, a2, a3
	addi	a3, zero, 10
	mul	a2, a2, a3
	sub	a0, a0, a2
	lh	a2, -52(s0)
	addi	a3, a2, 1
	sh	a3, -52(s0)
	slli	a2, a2, 1
	addi	a3, s0, -48
	add	a2, a3, a2
	sh	a0, 0(a2)
	lw	a0, -20(s0)
	mulh	a0, a0, a1
	srli	a1, a0, 31
	srai	a0, a0, 2
	add	a0, a0, a1
	sw	a0, -20(s0)
	j	.LBB6_5
.LBB6_7:
	lh	a0, -52(s0)
	lh	a1, -50(s0)
	add	a0, a0, a1
	addi	a0, a0, 1
	srai	a1, a0, 31
	call	malloc
	sw	a0, -56(s0)
	sh	zero, -58(s0)
	lhu	a0, -50(s0)
	beqz	a0, .LBB6_9
	j	.LBB6_8
.LBB6_8:
	lw	a0, -56(s0)
	addi	a1, zero, 45
	sb	a1, 0(a0)
	j	.LBB6_9
.LBB6_9:
	j	.LBB6_10
.LBB6_10:                               # =>This Inner Loop Header: Depth=1
	lh	a0, -58(s0)
	lh	a1, -52(s0)
	bge	a0, a1, .LBB6_12
	j	.LBB6_11
.LBB6_11:                               #   in Loop: Header=BB6_10 Depth=1
	lh	a0, -52(s0)
	lh	a1, -58(s0)
	sub	a0, a0, a1
	slli	a0, a0, 1
	addi	a2, s0, -48
	add	a0, a0, a2
	lh	a0, -2(a0)
	addi	a0, a0, 48
	lw	a2, -56(s0)
	lh	a3, -50(s0)
	add	a1, a1, a3
	add	a1, a2, a1
	sb	a0, 0(a1)
	lh	a0, -58(s0)
	addi	a0, a0, 1
	sh	a0, -58(s0)
	j	.LBB6_10
.LBB6_12:
	lw	a0, -56(s0)
	lh	a1, -52(s0)
	lh	a2, -50(s0)
	add	a1, a1, a2
	add	a0, a0, a1
	sb	zero, 0(a0)
	lw	a0, -56(s0)
	sw	a0, -16(s0)
	j	.LBB6_13
.LBB6_13:
	lw	a0, -16(s0)
	lw	s0, 56(sp)
	.cfi_def_cfa sp, 64
	lw	ra, 60(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 64
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end6:
	.size	toString, .Lfunc_end6-toString
	.cfi_endproc
                                        # -- End function
	.globl	string_length           # -- Begin function string_length
	.p2align	2
	.type	string_length,@function
string_length:                          # @string_length
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 16
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	lw	a0, -16(s0)
	call	strlen
	lw	s0, 8(sp)
	.cfi_def_cfa sp, 16
	lw	ra, 12(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 16
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end7:
	.size	string_length, .Lfunc_end7-string_length
	.cfi_endproc
                                        # -- End function
	.globl	string_substring        # -- Begin function string_substring
	.p2align	2
	.type	string_substring,@function
string_substring:                       # @string_substring
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	sw	a1, -20(s0)
	sw	a2, -24(s0)
	lw	a0, -24(s0)
	lw	a1, -20(s0)
	sub	a0, a0, a1
	addi	a0, a0, 1
	srai	a1, a0, 31
	call	malloc
	sw	a0, -32(s0)
	lw	a0, -32(s0)
	lw	a1, -16(s0)
	lw	a2, -20(s0)
	add	a1, a1, a2
	lw	a3, -24(s0)
	sub	a2, a3, a2
	call	memcpy
	lw	a0, -32(s0)
	lw	a1, -24(s0)
	lw	a2, -20(s0)
	sub	a1, a1, a2
	add	a0, a0, a1
	sb	zero, 0(a0)
	lw	a0, -32(s0)
	lw	s0, 24(sp)
	.cfi_def_cfa sp, 32
	lw	ra, 28(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 32
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end8:
	.size	string_substring, .Lfunc_end8-string_substring
	.cfi_endproc
                                        # -- End function
	.globl	string_parseInt         # -- Begin function string_parseInt
	.p2align	2
	.type	string_parseInt,@function
string_parseInt:                        # @string_parseInt
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	lw	a0, -16(s0)
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	addi	a2, s0, -20
	call	__isoc99_sscanf
	lw	a0, -20(s0)
	lw	s0, 24(sp)
	.cfi_def_cfa sp, 32
	lw	ra, 28(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 32
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end9:
	.size	string_parseInt, .Lfunc_end9-string_parseInt
	.cfi_endproc
                                        # -- End function
	.globl	string_ord              # -- Begin function string_ord
	.p2align	2
	.type	string_ord,@function
string_ord:                             # @string_ord
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	sw	a1, -20(s0)
	lw	a0, -16(s0)
	lw	a1, -20(s0)
	add	a0, a0, a1
	lb	a0, 0(a0)
	lw	s0, 24(sp)
	.cfi_def_cfa sp, 32
	lw	ra, 28(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 32
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end10:
	.size	string_ord, .Lfunc_end10-string_ord
	.cfi_endproc
                                        # -- End function
	.globl	_array_size             # -- Begin function _array_size
	.p2align	2
	.type	_array_size,@function
_array_size:                            # @_array_size
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -16
	.cfi_def_cfa_offset 16
	sw	ra, 12(sp)
	sw	s0, 8(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 16
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	lw	a0, -16(s0)
	lw	a0, -4(a0)
	lw	s0, 8(sp)
	.cfi_def_cfa sp, 16
	lw	ra, 12(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 16
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end11:
	.size	_array_size, .Lfunc_end11-_array_size
	.cfi_endproc
                                        # -- End function
	.globl	string_add              # -- Begin function string_add
	.p2align	2
	.type	string_add,@function
string_add:                             # @string_add
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -48
	.cfi_def_cfa_offset 48
	sw	ra, 44(sp)
	sw	s0, 40(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 48
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	sw	a1, -24(s0)
	lw	a0, -16(s0)
	call	strlen
	sw	a0, -28(s0)
	lw	a0, -24(s0)
	call	strlen
	sw	a0, -32(s0)
	lw	a0, -28(s0)
	lw	a1, -32(s0)
	add	a0, a0, a1
	addi	a0, a0, 1
	srai	a1, a0, 31
	call	malloc
	sw	a0, -40(s0)
	lw	a0, -40(s0)
	lw	a1, -16(s0)
	call	strcpy
	lw	a0, -40(s0)
	lw	a1, -24(s0)
	call	strcat
	lw	a0, -40(s0)
	lw	s0, 40(sp)
	.cfi_def_cfa sp, 48
	lw	ra, 44(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 48
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end12:
	.size	string_add, .Lfunc_end12-string_add
	.cfi_endproc
                                        # -- End function
	.globl	string_eq               # -- Begin function string_eq
	.p2align	2
	.type	string_eq,@function
string_eq:                              # @string_eq
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	sw	a1, -24(s0)
	lw	a0, -16(s0)
	lw	a1, -24(s0)
	call	strcmp
	seqz	a0, a0
	lw	s0, 24(sp)
	.cfi_def_cfa sp, 32
	lw	ra, 28(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 32
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end13:
	.size	string_eq, .Lfunc_end13-string_eq
	.cfi_endproc
                                        # -- End function
	.globl	string_ne               # -- Begin function string_ne
	.p2align	2
	.type	string_ne,@function
string_ne:                              # @string_ne
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	sw	a1, -24(s0)
	lw	a0, -16(s0)
	lw	a1, -24(s0)
	call	strcmp
	snez	a0, a0
	lw	s0, 24(sp)
	.cfi_def_cfa sp, 32
	lw	ra, 28(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 32
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end14:
	.size	string_ne, .Lfunc_end14-string_ne
	.cfi_endproc
                                        # -- End function
	.globl	string_lt               # -- Begin function string_lt
	.p2align	2
	.type	string_lt,@function
string_lt:                              # @string_lt
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	sw	a1, -24(s0)
	lw	a0, -16(s0)
	lw	a1, -24(s0)
	call	strcmp
	srli	a0, a0, 31
	lw	s0, 24(sp)
	.cfi_def_cfa sp, 32
	lw	ra, 28(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 32
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end15:
	.size	string_lt, .Lfunc_end15-string_lt
	.cfi_endproc
                                        # -- End function
	.globl	string_le               # -- Begin function string_le
	.p2align	2
	.type	string_le,@function
string_le:                              # @string_le
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	sw	a1, -24(s0)
	lw	a0, -16(s0)
	lw	a1, -24(s0)
	call	strcmp
	slti	a0, a0, 1
	lw	s0, 24(sp)
	.cfi_def_cfa sp, 32
	lw	ra, 28(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 32
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end16:
	.size	string_le, .Lfunc_end16-string_le
	.cfi_endproc
                                        # -- End function
	.globl	string_gt               # -- Begin function string_gt
	.p2align	2
	.type	string_gt,@function
string_gt:                              # @string_gt
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	sw	a1, -24(s0)
	lw	a0, -16(s0)
	lw	a1, -24(s0)
	call	strcmp
	sgtz	a0, a0
	lw	s0, 24(sp)
	.cfi_def_cfa sp, 32
	lw	ra, 28(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 32
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end17:
	.size	string_gt, .Lfunc_end17-string_gt
	.cfi_endproc
                                        # -- End function
	.globl	string_ge               # -- Begin function string_ge
	.p2align	2
	.type	string_ge,@function
string_ge:                              # @string_ge
	.cfi_startproc
# %bb.0:
	addi	sp, sp, -32
	.cfi_def_cfa_offset 32
	sw	ra, 28(sp)
	sw	s0, 24(sp)
	.cfi_offset ra, -4
	.cfi_offset s0, -8
	addi	s0, sp, 32
	.cfi_def_cfa s0, 0
	sw	a0, -16(s0)
	sw	a1, -24(s0)
	lw	a0, -16(s0)
	lw	a1, -24(s0)
	call	strcmp
	not	a0, a0
	srli	a0, a0, 31
	lw	s0, 24(sp)
	.cfi_def_cfa sp, 32
	lw	ra, 28(sp)
	.cfi_restore ra
	.cfi_restore s0
	addi	sp, sp, 32
	.cfi_def_cfa_offset 0
	ret
.Lfunc_end18:
	.size	string_ge, .Lfunc_end18-string_ge
	.cfi_endproc
                                        # -- End function
	.type	.L.str,@object          # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%s"
	.size	.L.str, 3

	.type	.L.str.1,@object        # @.str.1
.L.str.1:
	.asciz	"%s\n"
	.size	.L.str.1, 4

	.type	.L.str.2,@object        # @.str.2
.L.str.2:
	.asciz	"%d"
	.size	.L.str.2, 3

	.type	.L.str.3,@object        # @.str.3
.L.str.3:
	.asciz	"%d\n"
	.size	.L.str.3, 4


	.ident	"clang version 6.0.0-1ubuntu2 (tags/RELEASE_600/final)"
	.section	".note.GNU-stack","",@progbits
