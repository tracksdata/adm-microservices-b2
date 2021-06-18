
fetch('http://localhost:8081/api/pss/fare/1')
    .then(response => response.json())
    .then(fare => {
        console.log(fare);
    })