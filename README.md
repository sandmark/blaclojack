# blaclojack

練習で簡単なブラックジャックを Clojure で書いてみる。
参考: https://www.sejuku.net/blog/91446

## Specifications
カード枚数は 52 枚。ジョーカーは含めない。カードの重複が無いように山札を構築する。
プレイヤー、ディーラーの一対一で対戦するものとし、以下の挙動を取る
初期設定として、プレイヤー・ディーラーが交互に 1 枚ずつ山札からカードを取り手札とする。
プレイヤーからは自分の手札すべてと、ディーラーの 1 枚めの手札が確認できる。（ディーラーの 2 枚目移行の手札はわからない）

手札は A が 1 ポイント、 2-10 がそれぞれ 2-10 ポイント、 J/Q/K が 10 ポイントとして計算される。

プレイヤーは手札を 1 枚追加するか、しないかを選択できる。
手札を追加した場合、 21 ポイントを超えるとバーストとなり、ゲームに敗北する。
プレイヤーはバーストするか、好きなタイミングで止めるまで手札にカードを追加できる。
ディーラーは手札の合計ポイントが 17 以上になるまで山札を引き続ける。
ディーラーの手札が 21 ポイントを超えた場合、バーストしてプレイヤーの勝利。
ディーラーの手札が 18 以上 21 以下になったとき次の段階に移行する。

プレイヤー・ディーラーの手札のポイントを比較して、大きいほうが勝利。

ダブルダウンやスプリットなどの特殊ルールは無し。

## License

Copyright © 2023 sandmark

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
