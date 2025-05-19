import pandas as pd
import json

# 1. Dataset 가져오기
df = pd.read_csv("Emoji_Sentiment_Data_v1.0.csv")

# 2. 실제 서비스에서 사용할 감정 그룹별 이모지 정의 -> 해당 이모지만 추출하여 사용.
custom_emoji_groups = {
    "calm": ["😌", "😐", "😇"],
    "joy": ["😀", "😆", "🤣", "😊", "😍", "🙂"],
    "sad": ["😥", "😢", "😢", "😭"],
    "anger": ["😠", "😡", "😑", "😒", "🙄"]
}

# 3. 감정 그룹별 기본 강도 정의 -> 슬픔 감정의 경우 긍정적인 의미로도 사용되는 이모지인 경우가 많아
# 감정 강도가 낮음 -> 가중치를 둬서 프롬프트 작성에 용이하게 하기 위함.
base_intensity = {
    "joy": 1.0,
    "sad": 1.5,
    "anger": 1.0,
    "calm": 1.0
}

# 4. 이모지 → 감정 그룹 매핑
emoji_emotion_map = {
    emoji: group
    for group, emojis in custom_emoji_groups.items()
    for emoji in emojis
}

# 5. 필터링 및 intensity 계산
# pos, neg, neu 감정 점수 받아와서 감정 강도 계산
# pos, neg, neu 중 가장 큰 점수가 메인 감정이 됨
# 유효한 intensity 계산을 위해 위의 base_intensity 사용
filtered_map = {}

for _, row in df.iterrows():
    emoji = row["Emoji"]
    if emoji not in emoji_emotion_map:
        continue

    pos = int(row["Positive"])
    neg = int(row["Negative"])
    neu = int(row["Neutral"])
    total = int(row["Occurrences"])

    if total == 0:
        continue

    # dominant 감정 추출
    if pos > neg and pos > neu:
        dominant = pos
    elif neg > pos and neg > neu:
        dominant = neg
    else:
        dominant = neu

    emotion = emoji_emotion_map[emoji]
    base = base_intensity[emotion]

    # 최종 intensity 계산
    intensity = round((dominant / total) * base, 3)

    filtered_map[emoji] = {
        "emotion": emotion,
        "intensity": intensity
    }

# 6. JSON으로 저장
with open("emoji2emotion_filtered_baseintensity.json", "w", encoding="utf-8") as f:
    json.dump(filtered_map, f, ensure_ascii=False, indent=2)