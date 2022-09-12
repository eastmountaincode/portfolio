////Initialization

var winWidth = window.innerWidth;
var winHeight = window.innerHeight;

//MAKE THE AUDIO PLAYER APPEAR AT A FIXED RANDOM LOCATION
//(SO THE USER CAN'T JUST RESIZE THE WEBPAGE AND FIND IT)


//center the text and get the coordinates relative to the viewport
var actualaudiobox = document.querySelector(".actualaudioplayer")
var rect = actualaudiobox.getBoundingClientRect();
var top = rect.top
var left = rect.left
console.log("Top:", rect.top, "Right:", rect.right, "Bottom:", rect.bottom, "Left:", rect.left);

// //switch the classname, turning position to absolute
var audiobox = document.querySelector(".audioplayer")
audiobox.className = "audioplayerswitch"

var audiobox = document.querySelectorAll(".audioplayerswitch")
var thisDiv = audiobox[0];

thisDiv.style.top = top +"px";
thisDiv.style.left = left +"px";


//MAKE ALL IMG TAGS APPEAR AT RANDOM LOCATIONS ON THE VIEWPORT

// collect all the divs
var divs = document.querySelectorAll(".draggable");
// get window width and height
  //-->gotten in initialization

// i stands for "index". you could also call this banana or haircut. it's a variable
for ( var i=0; i < divs.length; i++ ) {
  
    // shortcut! the current div in the list
    var thisDiv = divs[i];

    // find image dimensions so it doesn't get placed outside the 
    // viewport
    var img = thisDiv;

    var imgwidth = img.naturalWidth
    var imgheight = img.naturalHeight
    console.log("Width: ", imgwidth, "Height: ", imgheight)
    
    // get random numbers for each element
    randomTop = getRandomNumber(0 + 60, winHeight - (imgheight/2));
    randomLeft = getRandomNumber(0 + 20, winWidth - (imgwidth/2));

    // update top and left position
    thisDiv.style.top = randomTop +"px";
    thisDiv.style.left = randomLeft +"px";
    
}

// function that returns a random number between a min and max
function getRandomNumber(min, max) {
  
  return Math.random() * (max - min) + min;
    
}

//MAKE ITEMS DRAGGABLE

interact('.draggable')
  .draggable({
    onmove: function(event) {
      const target = event.target;

      const dataX = target.getAttribute('data-x');
      const dataY = target.getAttribute('data-y');
      const initialX = parseFloat(dataX) || 0;
      const initialY = parseFloat(dataY) || 0;

      const deltaX = event.dx;
      const deltaY = event.dy;

      const newX = initialX + deltaX;
      const newY = initialY + deltaY;

      target
        .style
        .transform = `translate(${newX}px, ${newY}px)`;

      target.setAttribute('data-x', newX);
      target.setAttribute('data-y', newY);
    }
  })

//MAKE ITEMS RESIZABLE

// interact('.draggable')
//   .resizable({
//     edges: { top: true, left: true, bottom: true, right: true },
//     listeners: {
//       move: function (event) {
//         let { x, y } = event.target.dataset

//         x = (parseFloat(x) || 0) + event.deltaRect.left
//         y = (parseFloat(y) || 0) + event.deltaRect.top

//         Object.assign(event.target.style, {
//           width: `${event.rect.width}px`,
//           height: `${event.rect.height}px`,
//           transform: `translate(${x}px, ${y}px)`
//         })

//         Object.assign(event.target.dataset, { x, y })
//       }
//     }
//   })
