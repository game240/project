package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Poker {
    public static int cardRank(LinkedList<Card> cards) {
        /*
         * 다섯 장을 판별하는 방법:
         * 1번째 카드: 2~5번째 카드와 비교
         * 2번째 카드: 3~5번째 카드와 비교
         * 3번째 카드: 4~5번째 카드와 비교
         * 4번째 카드: 5번째 카드와 비교
         * 
         * i == 0, j == 1~4
         * i == 1, j == 2~4
         * i == 2, j == 3~4
         * i == 3, j == 4
         */
        int pairCount = 0;
        for (int i = 0; i < 4; ++i) {
            for (int j = i + 1; j < 5; ++j) {
                if (cards.get(i).number == cards.get(j).number) {
                    pairCount++;
                    /*
                     * 원페어: 1
                     * 투페어: 2
                     * 트리플: 3
                     * 풀하우스: 4 ∵ 3 + 1
                     * 포카드: 6 ∵ 2s, 2h, 2d, 2c, 3c일 때 똑같은 숫자의 쌍: 2s-2h, 2s-2d, 2s-2c, 2h-2d, 2h-2c, 2d-2c
                     */
                }
            }
        }

        // straight 여부 판정
        LinkedList<Integer> sortedNum = new LinkedList<>(); // sortedNum: 숫자별 정렬
        for (int i = 0; i < 5; ++i) {
            sortedNum.add(cards.get(i).number); // sortedNum에 값 복사: cards의 number값
        }
        Collections.sort(sortedNum);

        boolean straight = false;
        if (pairCount == 0) {
            // [1, 2, 3, 4, 5], [2, 3, 4, 5, 6], …, [9, 10, 11, 12, 13]: 가장 큰 수와 가장 작은 수의 차가 4
            if (sortedNum.get(4) - sortedNum.get(0) == 4) {
                straight = true;
            }
            if (sortedNum.get(0) == 1 && sortedNum.get(1) == 10) { // [A, 10, J, Q, K]
                straight = true;
            }
        }

        // flush 여부 판정
        LinkedList<String> sortedSuit = new LinkedList<>(); // suit: 문양별 정렬
        for (int i = 0; i < 5; ++i) {
            sortedSuit.add(cards.get(i).suit); // sortedSuit에 값 복사: cards의 suit값
        }
        Collections.sort(sortedSuit);

        // 문양별로 정렬한 후 suit[0]과 suit[4]가 같을 경우, index 0~4까지 같은 문양
        boolean flush = false;
        if (sortedSuit.get(0).equals(sortedSuit.get(4))) {
            flush = true;
        }

        // rank: 숫자가 작을 수록 높은 순위
        int rank;
        if (straight && flush) {
            rank = 1;
        } else if (pairCount == 6) {
            rank = 2;
        } else if (pairCount == 4) {
            rank = 3;
        } else if (flush) {
            rank = 4;
        } else if (straight) {
            rank = 5;
        } else if (pairCount == 3) {
            rank = 6;
        } else if (pairCount == 2) {
            rank = 7;
        } else if (pairCount == 1) {
            rank = 8;
        } else {
            rank = 9;
        }

        return rank;
    }
    
    public static int bestRank(LinkedList<Card> cards) {
        int bestRank = 9; // 가장 낮은 랭크로 초기화
        /*
         * 7장의 카드 중 5장을 선택하는 모든 조합을 확인
         * i = 0, j = 1 ~ 6까지 검사
         * i = 1, j = 2 ~ 6까지 검사
         * ...
         * i = 5, j = 6까지 검사
         */ 
    
        for (int i = 0; i < cards.size(); ++i) {
            for (int j = i + 1; j < cards.size(); ++j) {
                LinkedList<Card> temp = new LinkedList<>(cards); // temp에 cards의 값 복사
                
                // i, j의 카드 제외: 뒤의 index부터 제거해야 오류 X
                temp.remove(j);
                temp.remove(i);

                int rank = cardRank(temp); // 선별된 5장의 카드 랭크 계산
                if (rank < bestRank) {
                    bestRank = rank; // 더 좋은 랭크가 나오면 bestRank에 업데이트
                }
            }
        }
        return bestRank;
    }
    
    static void displayResult(int rank) {
        String rankString;
        switch (rank) {
            case 1:
                rankString = "Straight Flush";
                break;
            case 2:
                rankString = "Four of a Kind";
                break;
            case 3:
                rankString = "Full House";
                break;
            case 4:
                rankString = "Flush";
                break;
            case 5:
                rankString = "Straight";
                break;
            case 6:
                rankString = "Three of a Kind";
                break;
            case 7:
                rankString = "Two Pair";
                break;
            case 8:
                rankString = "One Pair";
                break;
            default:
                rankString = "High Card";
                break;
        }

        System.out.println("Rank: " + rankString);
    }

    public static void determineWinner(Player[] players) {
    int minRank = Integer.MAX_VALUE; // 초기 최소 랭크값 설정
    List<Integer> winnersIndex = new ArrayList<>(); // 우승자의 인덱스를 저장할 리스트

    // 각 플레이어의 최종 순위를 계산하여 최소 랭크를 찾음
    for (int i = 0; i < players.length; i++) {
        int rank = bestRank(players[i].hands); // 최종 랭크 계산
        if (rank < minRank) {
            minRank = rank; // 더 낮은 순위 발견 시 최소 랭크 업데이트
            winnersIndex.clear(); // 이전 우승자 인덱스 초기화
            winnersIndex.add(i); // 현재 플레이어를 우승자로 설정
        } else if (rank == minRank) {
            winnersIndex.add(i); // 최소 랭크를 가진 플레이어를 우승자로 추가
        }
    }

    // 우승자 출력
    if (winnersIndex.size() == 1) {
        System.out.println("우승자는 플레이어 " + (winnersIndex.get(0) + 1) + "입니다!");
    } else {
        // 무승부 판별
        boolean draw = true;
        for (int i = 1; i < winnersIndex.size(); i++) {
            if (!players[winnersIndex.get(i)].hands.equals(players[winnersIndex.get(0)].hands)) {
                draw = false;
                break;
            }
        }
        if (draw) {
            System.out.println("무승부입니다!");
        } else {
            // 최소 랭크를 가진 플레이어 출력
            System.out.print("동점자: ");
            for (int i = 0; i < winnersIndex.size(); i++) {
                System.out.print("플레이어 " + (winnersIndex.get(i) + 1) + ", ");
            }
            System.out.println("입니다!");
        }
    }
}
    
    // 플레이어 다수 생성
    static Player[] playerGenerating(int n) {
        Player[] players = new Player[n];
        for (int i = 0; i < n; ++i) {
            players[i] = new Player();
        }
        return players;
    }

    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.initialize(); // 초기 설정
        deck.tableSet();

        Scanner scanner = new Scanner(System.in);
        System.out.println("플레이어 수를 입력해주세요. ");
        int n = scanner.nextInt();

        Player[] players = new Player[n];
        players = playerGenerating(n);

        for (int i = 0; i < n; ++i) {
            players[i].drawing();
            players[i].getHands();
            int rank = bestRank(players[i].hands);
            displayResult(rank);
        }

        determineWinner(players);

        scanner.close();
    }
}