<template>
    <div class="chat-container">
      <div class="chat-header">小易为您提供帮助</div>
      <div class="chat-messages" ref="chatMessages">
        <div v-for="(message, index) in messages" :key="index" class="chat-message" :class="{ 'user-message': message.isFromUser }">
          {{ message.content }}
        </div>
      </div>
      <div class="chat-input">
        <input type="text" v-model="message" placeholder="请输入您的问题" @keyup.enter="sendMessage"/>
        <button @click="sendMessage">发送</button>
      </div>
    </div>
  </template>

  <script>
  const questionsAndAnswers = [
                   {
                    question: '如何在该平台注册并登录？',
                    answer: '您可以通过注册页面填写相关信息来注册账号，注册成功后即可使用登录功能。',
                    keywords: ['注册', '登录', '平台'],
                    },
                    {
                    question: '我是社会人士，如何在该平台报名自考大学专业？',
                    answer: '您可以在平台上选择院校和课程，并填写相关信息进行报名。',
                    keywords: ['社会人士', '自考大学专业', '报名', '平台'],
                    },
                    {
                    question: '我是专科在读学生，能够在该平台报名自考大学专业吗？',
                    answer: '是的，您可以在该平台进行自考大学专业的报名。',
                    keywords: ['专科在读学生', '自考大学专业', '报名', '平台'],
                    },
                    {
                    question: '如何管理自考大学专业？',
                    answer: '各高校可以在系统中设置自己院校的专业课程、发布相关的考试公告等，并对已经报名的考生进行审核。',
                    keywords: ['自考大学专业', '管理', '高校', '审核', '平台'],
                    },
                    {
                    question: '我忘记了在该平台的密码，该怎么办？',
                    answer: '您可以通过邮箱找回密码，平台会发送重置密码链接至您的注册邮箱。',
                    keywords: ['忘记密码', '密码', '邮箱', '平台'],
                    },
                    {
                    question: '如何查询考试公告？',
                    answer: '您可以在平台上搜索相关院校发布的公告，并通过关键词筛选掉无关的公告，了解其相关的考核情况。',
                    keywords: ['公告', '查询', '平台'],
                    },
                    {
                    question: '这个平台是否支持数据统计和导出功能？',
                    answer: '是的，该平台支持对报名、审核、结果查询等数据进行统计和导出，方便各高校进行相关管理。',
                    keywords: ['数据统计', '导出功能', '高校', '平台'],
                    },
                    {
                    question: '该平台是否支持在线支付报名费用？',
                    answer: '目前该平台暂不支持在线支付，您需要前往当地教育考试院，提交相关材料并缴纳考试费用。',
                    keywords: ['在线支付', '报名费用', '教育考试院', '平台'],
                    },
                    {
                    question: '你好吗？',
                    answer: '我是一个AI语言模型，没有感情，但我随时准备为您解答问题。',
                    keywords: ['你好', 'AI','好吗'],
                    },
                    {
                    question: '谢谢',
                    answer: '不客气，有任何问题随时可以问我。',
                    keywords: ['谢', '回答'],
                    },
                {
                question: '如何在该平台进行注册？',
                answer: '用户可在平台首页点击“注册”按钮，填写相关信息并提交即可完成注册。',
                keywords: ['注册'],
                },
                {
                question: '注册需要填写哪些信息？',
                answer: '注册需要填写用户名、密码、电子邮箱等基本信息。',
                keywords: ['注册', '信息'],
                },
                {
                question: '如何登录该平台？',
                answer: '用户可在平台首页点击“登录”按钮，输入用户名和密码即可登录。',
                keywords: ['登录'],
                },
                {
                question: '如何报名自考大学专业？',
                answer: '用户可在平台上搜索相关院校和专业，选择想要报名的专业并填写相关信息即可完成报名。',
                keywords: ['报名', '自考大学专业'],
                },
                {
                question: '自考大学专业需要缴纳多少报名费？',
                answer: '不同院校和专业的报名费用不同，请在报名时查看相应的费用标准。',
                keywords: ['费用', '自考大学专业'],
                },
                {
                question: '如何查询自考大学专业的考试时间和地点？',
                answer: '考试时间和地点会在考试公告中发布，考生可在平台上搜索相关院校发布的公告，查看相应信息。',
                keywords: ['考试时间', '考试地点', '自考大学专业'],
                },
                {
                question: '如何修改已经提交的报名信息？',
                answer: '如需修改已经提交的报名信息，请联系平台客服进行修改。',
                keywords: ['修改', '报名信息'],
                },
                {
                question: '忘记登录密码怎么办？',
                answer: '用户可点击登录页面下方的“忘记密码”链接，按照提示进行密码重置。',
                keywords: ['密码', '登录'],
                },
                {
                question: '忘记注册邮箱怎么办？',
                answer: '如无法记起注册邮箱，请联系平台客服进行邮箱修改。',
                keywords: ['邮箱', '注册'],
                },
                {
                question: '如何查询自考大学专业的考试成绩？',
                answer: '考试成绩会在考试后发布，考生可在平台上搜索相关院校发布的公告，查看相应信息。',
                keywords: ['考试成绩', '自考大学专业'],
                },
                {
                question: '如何联系平台客服？',
                answer: '用户可在平台首页点击“联系客服”按钮，填写相关信息并提交即可联系平台客服。',
                keywords: ['客服', '联系'],
                },
                {
                question: '如何查询自考大学专业的报名结果？',
                answer: '报名结果会在报名审核后发布，考生可在平台上搜索相关院校发布的公告，查看相应信息。',
                keywords: ['结果', '自考大学专业'],
                },
                {
                    question: '如何查询自考成绩？',
                    answer: '考生可在自考考试成绩查询网站上查询自己的考试成绩。',
                    keywords: [ '成绩', '查询'],
                    },
                    {
                    question: '自考的考试形式是什么？',
                    answer: '自考的考试形式包括笔试和口试两种。',
                    keywords: ['考试', '形式'],
                    },
                    {
                    question: '自考可以报考哪些学历层次？',
                    answer: '自考可以报考本科、专科、高中起点等不同学历层次。',
                    keywords: [ '学历层次', '报考'],
                    },
                    {
                    question: '自考的考试科目有哪些？',
                    answer: '自考的考试科目包括政治、英语、数学、专业课等。',
                    keywords: ['科目'],
                    },
                    {
                    question: '自考的报名时间是什么时候？',
                    answer: '自考的报名时间根据不同的院校和地区有所不同，一般在每年的3月份和9月份。',
                    keywords: ['报名时间'],
                    },
                    {
                    question: '自考考试的通过率如何？',
                    answer: '自考考试的通过率因地区和科目不同而异，一般在20%~50%之间。',
                    keywords: ['自考', '考试', '通过率'],
                    },
                    {
                    question: '自考需要交多少学费？',
                    answer: '自考的学费根据不同学校和专业有所不同，一般在几千元到几万元之间。',
                    keywords: [ '学费'],
                    },
                    {
                    question: '自考的考试时间是什么时候？',
                    answer: '自考的考试时间根据不同学校和地区有所不同，一般在每年的6月份和12月份。',
                    keywords: [ '考试时间','时间'],
                    },
                    {
                    question: '自考的考试科目和普通高考有什么区别？',
                    answer: '自考的考试科目和普通高考基本一致，但自考的考试内容更加专业化。',
                    keywords: [ '科目', '区别'],
                    },
                    {
                    question: '自考可以报考哪些专业？',
                    answer: '自考可以报考的专业包括工商管理、会计学、法律、教育学、信息管理与信息系统、汉语言文学等多个专业。',
                    keywords: ['报考', '专业'],
                    },
                    {
                    question: '自考的考试科目需要预先报名吗？',
                    answer: '需要。考生需要在规定的时间内进行考试科目的报名。',
                    keywords: ['预先报名','预']
                    }
    // ... 其他问题和答案
  ];

  export default {
    data() {
      return {
        message: '',
        messages: [],
      };
    },
    methods: {
      sendMessage() {
        const message = this.message.trim();
        if (message.length === 0) {
          return;
        }
        this.messages.push({
          content: message,
          isFromUser: true,
        });
        this.message = '';
        this.scrollToBottom();
        this.processAnswer(message);
      },
      processAnswer(question) {
        let foundAnswer = null;
        for (const qa of questionsAndAnswers) {
          for (const keyword of qa.keywords) {
            if (question.indexOf(keyword) !== -1) {
              foundAnswer = qa.answer;
              break;
            }
          }
          if (foundAnswer !== null) {
            break;
          }
        }
        if (foundAnswer === null) {
          foundAnswer = '抱歉，我无法回答您的问题。';
        }
        setTimeout(() => {
          this.messages.push({
            content: foundAnswer,
            isFromUser: false,
          });
          this.scrollToBottom();
        }, 500);
      },
      scrollToBottom() {
            this.$nextTick(() => {
                this.$refs.chatMessages.scrollTop = this.$refs.chatMessages.scrollHeight;
            });
            },
    },
  };
</script>

  
  <style>
  .chat-container {
    
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);

    display: flex;
    flex-direction: column;
    
    height: 300px;
    width: 700px;
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    border-radius: 10px;
    font-family: "Helvetica Neue", "Helvetica", "Arial", sans-serif;
  }
  
  .chat-header {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 50px;
    width: 100%;
    background-color: #4CAF50;
    color: #fff;
    font-size: 20px;
    font-weight: bold;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
  }
  
  .chat-messages {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-end;
    height: 80%;
    width: 100%;
    padding: 10px;
    overflow-y: scroll;
  }
  
  .chat-message {
    margin-bottom: 10px;
    padding: 10px;
    max-width: 60%;
    border-radius: 10px;
    background-color: #eee;
  }
  
  .chat-message.user-message {
    align-self: flex-end;
    background-color: #4CAF50;
    color: #fff;
  }
  
  .chat-input {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 70px;
    width: 100%;
    padding: 10px;
  }
  
  .chat-input input {
    height: 40px;
    width: 80%;
    border-radius: 20px;
    border: none;
    padding: 0 10px;
    font-size: 16px;
  }
  
  .chat-input button {
    height: 40px;
    width: 20%;
    border-radius: 20px;
    border: none;
    background-color: #4CAF50;
    color: #fff;
    font-size: 16px;
    margin-left: 10px;
    cursor: pointer;
  }
  </style>